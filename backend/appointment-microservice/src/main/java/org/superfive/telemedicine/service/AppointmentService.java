package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.exception.*;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Availability;
import org.superfive.telemedicine.repository.AppointmentRepository;
import org.superfive.telemedicine.repository.AvailabilityRepository;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    private final AvailabilityRepository availabilityRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, AvailabilityRepository availabilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.availabilityRepository = availabilityRepository;
    }

    // Get all appointments
    public Page<Appointment> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAllBy(pageable);
    }

    // Get an appointment by ID
    public Appointment getAppointmentByID(int appointmentID) throws ResourceNotFoundException {
        return appointmentRepository.findByAppointmentID(appointmentID)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "appointmentID", appointmentID));
    }

    // Get a doctor's appointments
    public Page<Appointment> getDoctorAppointments(int doctorID, Pageable pageable) {
        return appointmentRepository.findByDoctorID(doctorID, pageable);
    }

    // Get a patient's appointments
    public Page<Appointment> getPatientAppointments(int patientID, Pageable pageable) {
        return appointmentRepository.findByPatientID(patientID, pageable);
    }

    @Transactional
    // Add a new appointment
    public Appointment addAppointment(Appointment appointment) throws ResourceAlreadyExistsException,
            InvalidTimeException, EntityTimeClashException {
        // Ensure appointment ID does not already exist
        try {
            this.getAppointmentByID(appointment.getAppointmentID());
            throw new ResourceAlreadyExistsException("Appointment", "appointmentID", appointment.getAppointmentID());
        } catch (ResourceNotFoundException ex) {
            // appointment ID does not exist so new appointment can be added
        }

        // Ensure start time comes before end time
        LocalTime appointmentStartTime = appointment.getStartTime();
        LocalTime appointmentEndTime = appointment.getEndTime();

        if (appointmentStartTime.isAfter(appointmentEndTime)) {
            throw new InvalidTimeException(appointmentStartTime, appointmentEndTime);
        }

        // Ensure appointment is made at a valid date and time
        List<Availability> doctorAvailabilities = availabilityRepository.findByDoctorID(appointment.getDoctorID(), null).getContent();
        List<Appointment> patientAppointments = appointmentRepository.findByPatientID(appointment.getPatientID(), null).getContent();

        LocalDate appointmentDate = appointment.getDate();
        DayOfWeek appointmentDayOfWeek = appointmentDate.getDayOfWeek();

        boolean haveStartAndEndTimeAvailability = false;

        // Ensure appointment is made at a time when doctor is available
        // Check for a particular day of the week
        for (Availability availability : doctorAvailabilities) {

            if (availability.getDayOfWeek().equals(appointmentDayOfWeek)) {

                // Check if start time and end time is within availability start and end time
                if (!appointmentStartTime.isBefore(availability.getStartTime()) &&
                        !appointmentStartTime.isAfter(appointmentEndTime) &&
                        !appointmentEndTime.isBefore(availability.getStartTime()) &&
                        !appointmentEndTime.isAfter(availability.getEndTime())) {

                    haveStartAndEndTimeAvailability = true;

                    // Doctor is available if not yet booked
                    // Ensure that there are no other appointments clashing with the time
                    // Check if doctor is already booked

                    // Get doctor's appointments
                    List<Appointment> doctorAppointments = appointmentRepository.findByDoctorID(appointment.getDoctorID(), null).getContent();
                    // Search any of the doctor appointments that fall on the same date as the new appointment
                    for (Appointment doctorAppointment : doctorAppointments) {
                        if (doctorAppointment.getDate().equals(appointmentDate)) {
                            // Check for a time clash

                            // If newAppointment start time is before appointment start time, then newAppointment
                            // end time must be before or equal to appointment start time
                            if (appointmentStartTime.isBefore(doctorAppointment.getStartTime()) &&
                                    !appointmentEndTime.isAfter(doctorAppointment.getStartTime())) {
                                continue;
                            }
                            // Else If newAppointment start time is after or equal to appointment end time, then newAppointment
                            // end time must be after appointment end time
                            else if (!appointmentStartTime.isBefore(doctorAppointment.getEndTime()) &&
                                    appointmentEndTime.isAfter(doctorAppointment.getEndTime())) {
                                continue;
                            } else {
                                // Throw exception here for doctor time clash
                                throw new EntityTimeClashException("Doctor", appointment.getDoctorID(),
                                        doctorAppointment.getStartTime(), doctorAppointment.getEndTime(),
                                        appointmentStartTime, appointmentEndTime);
                            }
                        }
                    }

                    // Check if patient has no other appointments during appointment time
                    // Search any of the patient appointments that fall on the same date as the new appointment
                    for (Appointment patientAppointment : patientAppointments) {
                        if (patientAppointment.getDate().equals(appointmentDate)) {
                            // Check for a time clash

                            // If newAppointment start time is before appointment start time, then newAppointment
                            // end time must be before or equal to appointment start time
                            if (appointmentStartTime.isBefore(patientAppointment.getStartTime()) &&
                                    !appointmentEndTime.isAfter(patientAppointment.getStartTime())) {
                                continue;
                            }
                            // Else If newAppointment start time is after or equal to appointment end time, then newAppointment
                            // end time must be after appointment end time
                            else if (!appointmentStartTime.isBefore(patientAppointment.getEndTime()) &&
                                    appointmentEndTime.isAfter(patientAppointment.getEndTime())) {
                                continue;
                            } else {
                                // Throw exception here for patient time clash
                                throw new EntityTimeClashException("Patient", appointment.getPatientID(),
                                        patientAppointment.getStartTime(), patientAppointment.getEndTime(),
                                        appointmentStartTime, appointmentEndTime);
                            }
                        }
                    }
                } else {
                    // Doctor is not available within those times
                    // Check next availability
                    continue;
                }
            } else {
                // Availability dayOfWeek is not equal to newAppointment dayofweek
                // Check next availability
                continue;
            }
        }

        if (!haveStartAndEndTimeAvailability) {
            // Doctor is not available
            throw new DoctorUnavailableException(appointment.getDoctorID(), appointmentDayOfWeek, appointmentStartTime,
                    appointmentEndTime);
        }

        appointmentRepository.save(appointment);
        return appointment;
    }

    @Transactional
    // Cancel an existing appointment by ID
    public Appointment cancelAppointment(int appointmentID) {
        Appointment deletedAppointment = this.getAppointmentByID(appointmentID);

        appointmentRepository.deleteById(appointmentID);

        return deletedAppointment;
    }
}
