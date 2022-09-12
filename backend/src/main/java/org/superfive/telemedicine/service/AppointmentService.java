package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.controller.DoctorController;
import org.superfive.telemedicine.controller.PatientController;
import org.superfive.telemedicine.exception.*;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Availability;
import org.superfive.telemedicine.model.Doctor;
import org.superfive.telemedicine.model.Patient;
import org.superfive.telemedicine.repository.AppointmentRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;


@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, DoctorService doctorService,
                              PatientService patientService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
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

    // Add a new appointment
    public Appointment addAppointment(Appointment appointment) throws ResourceAlreadyExistsException,
            InvalidTimeException, EntityTimeClashException {
        // Ensure appointment ID does not already exist
        try {
            this.getAppointmentByID(appointment.getAppointmentID());
            throw new ResourceAlreadyExistsException("Appointment", "appointmentID", appointment.getAppointmentID());
        }
        catch(ResourceNotFoundException ex) {
            // appointment ID does not exist so new appointment can be added
        }

        // Ensure start time comes before end time
        LocalTime appointmentStartTime = appointment.getStartTime();
        LocalTime appointmentEndTime = appointment.getEndTime();

        if (appointmentStartTime.isAfter(appointmentEndTime)) {
            throw new InvalidTimeException(appointmentStartTime, appointmentEndTime);
        }

        // Ensure appointment is made at a valid date and time
        System.out.println("NEW APPOINTMENT");
        Doctor doctor = doctorService.getDoctorByID(appointment.getDoctor().getUserID());
        System.out.println(doctor);
        Set<Availability> doctorAvailabilities = doctor.getAvailabilities();
        System.out.println(doctorAvailabilities);
        Patient patient = patientService.getPatientByID(appointment.getPatient().getUserID());

        LocalDate appointmentDate = appointment.getDate();
        DayOfWeek appointmentDayOfWeek = appointmentDate.getDayOfWeek();

        boolean haveStartAndEndTimeAvailability = false;
        boolean haveDayOfWeekAvailability = false;

        // Ensure appointment is made at a time when doctor is available
        // Check for a particular day of the week
        for (Availability availability : doctorAvailabilities) {
            System.out.println(String.format("Doctor Availability: %s", availability));
            if (availability.getDayOfWeek().equals(appointmentDayOfWeek)) {
                System.out.println(String.format("Day Of Week available: %s", appointmentDayOfWeek));

                haveDayOfWeekAvailability = true;
                // Check if start time and end time is within availability start and end time
                if (!appointmentStartTime.isBefore(availability.getStartTime()) &&
                    !appointmentStartTime.isAfter(appointmentEndTime) &&
                    !appointmentEndTime.isBefore(availability.getStartTime()) &&
                    !appointmentEndTime.isAfter(availability.getEndTime())) {
                    haveStartAndEndTimeAvailability = true;

                    System.out.println(String.format("Start time and end time available: %s - %s", availability.getStartTime(), availability.getEndTime()));
                    System.out.println(String.format("Start time and end time of appointment: %s - %s", appointmentStartTime, appointmentEndTime));

                    // Doctor is available if not yet booked
                    // Ensure that there are no other appointments clashing with the time
                    // Check if doctor is already booked

                    // Get doctor's appointments
                    Set<Appointment> doctorAppointments = doctor.getAppointments();
                    // Search any of the doctor appointments that fall on the same date as the new appointment
                    for (Appointment doctorAppointment : doctorAppointments) {
                        System.out.println(String.format("Doctor's appointment: %s", doctorAppointment));

                        if (doctorAppointment.getDate().equals(appointmentDate)) {
                            System.out.println(String.format("Date of doctor appointment: %s", appointmentDate));
                            // Check for a time clash

                            // If newAppointment start time is before appointment start time, then newAppointment
                            // end time must be before appointment start time
                            if (appointmentStartTime.isBefore(doctorAppointment.getStartTime()) &&
                                    appointmentEndTime.isBefore(doctorAppointment.getStartTime())) {
                                System.out.println(String.format("Time range valid 1: %s - %s", appointmentStartTime, appointmentEndTime));
                                continue;
                            }
                            // Else If newAppointment start time is after appointment end time, then newAppointment
                            // end time must be after appointment end time
                            else if (appointmentStartTime.isAfter(doctorAppointment.getEndTime()) &&
                                    appointmentEndTime.isAfter(doctorAppointment.getEndTime())) {
                                System.out.println(String.format("Time range valid 2: %s - %s", appointmentStartTime, appointmentEndTime));
                                continue;
                            }
                            else {
                                // Throw exception here for doctor time clash
                                throw new EntityTimeClashException("Doctor", doctor.getUserID(),
                                        doctorAppointment.getStartTime(), doctorAppointment.getEndTime(),
                                        appointmentStartTime, appointmentEndTime);
                            }
                        }
                    }

                    // Check if patient has no other appointments during appointment time
                    Set<Appointment> patientAppointments = patient.getAppointments();
                    // Search any of the patient appointments that fall on the same date as the new appointment
                    for (Appointment patientAppointment : patientAppointments) {
                        if (patientAppointment.getDate().equals(appointmentDate)) {
                            System.out.println(String.format("Date of patient appointment: %s", appointmentDate));
                            // Check for a time clash

                            // If newAppointment start time is before appointment start time, then newAppointment
                            // end time must be before appointment start time
                            if (appointmentStartTime.isBefore(patientAppointment.getStartTime()) &&
                                    appointmentEndTime.isBefore(patientAppointment.getStartTime())) {
                                System.out.println(String.format("Time range valid 1: %s - %s", appointmentStartTime, appointmentEndTime));
                                continue;
                            }
                            // Else If newAppointment start time is after appointment end time, then newAppointment
                            // end time must be after appointment end time
                            else if (appointmentStartTime.isAfter(patientAppointment.getEndTime()) &&
                                    appointmentEndTime.isAfter(patientAppointment.getEndTime())) {
                                System.out.println(String.format("Time range valid 2: %s - %s", appointmentStartTime, appointmentEndTime));
                                continue;
                            }
                            else {
                                // Throw exception here for patient time clash
                                throw new EntityTimeClashException("Patient", patient.getUserID(),
                                        patientAppointment.getStartTime(), patientAppointment.getEndTime(),
                                        appointmentStartTime, appointmentEndTime);
                            }
                        }
                    }
                }
                else {
                    // Doctor is not available within those times
                    // Check next availability
                    continue;
                }
            }
            else {
                // Availability dayOfWeek is not equal to newAppointment dayofweek
                // Check next availability
                continue;
            }
        }

        if (!(haveStartAndEndTimeAvailability && haveDayOfWeekAvailability)) {
            // Doctor is not available
            throw new DoctorUnavailableException(doctor.getUserID(), appointmentDayOfWeek, appointmentStartTime,
                    appointmentEndTime);
        }

        appointmentRepository.save(appointment);
        return appointment;
    }

    // Reschedule an existing appointment by ID
    public Appointment rescheduleAppointment(int appointmentID, Appointment updatedAppointment) throws
            InvalidTimeException {
        Appointment newAppointment = this.getAppointmentByID(appointmentID);

        // Check if attributes are null before updating
        if (updatedAppointment.getDate() != null) {
            newAppointment.setDate(updatedAppointment.getDate());
        }

        if (updatedAppointment.getStartTime() != null) {
            newAppointment.setStartTime(updatedAppointment.getStartTime());
        }

        if (updatedAppointment.getEndTime() != null) {
            newAppointment.setEndTime(updatedAppointment.getEndTime());
        }

        if (updatedAppointment.getAppointmentStatus() != null) {
            newAppointment.setAppointmentStatus(updatedAppointment.getAppointmentStatus());
        }

        if (updatedAppointment.getDoctor() != null) {
            newAppointment.setDoctor(updatedAppointment.getDoctor());
        }

        if (updatedAppointment.getPatient() != null) {
            newAppointment.setPatient(updatedAppointment.getPatient());
        }

        // Ensure times will still be valid after update
        if (newAppointment.getStartTime().isAfter(newAppointment.getEndTime())) {
            throw new InvalidTimeException(newAppointment.getStartTime(), newAppointment.getEndTime());
        }

        // TODO: Updated appointments have to be within doctor's availabilities

        // TODO: Updated appointments must not clash with existing appointments

        appointmentRepository.save(newAppointment);

        return newAppointment;
    }

    // Cancel an existing appointment by ID
    public Integer cancelAppointment(int appointmentID) {
        appointmentRepository.deleteById(appointmentID);

        return appointmentID;
    }


}
