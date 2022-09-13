package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
//        System.out.println("NEW APPOINTMENT");
        Doctor doctor = doctorService.getDoctorByID(appointment.getDoctor().getUserID());
        Set<Availability> doctorAvailabilities = doctor.getAvailabilities();
        Patient patient = patientService.getPatientByID(appointment.getPatient().getUserID());

        LocalDate appointmentDate = appointment.getDate();
        DayOfWeek appointmentDayOfWeek = appointmentDate.getDayOfWeek();

        boolean haveStartAndEndTimeAvailability = false;

        // Ensure appointment is made at a time when doctor is available
        // Check for a particular day of the week
        for (Availability availability : doctorAvailabilities) {
//            System.out.println(String.format("Doctor Availability: %s", availability));

            if (availability.getDayOfWeek().equals(appointmentDayOfWeek)) {
//                System.out.println(String.format("Day Of Week available: %s", appointmentDayOfWeek));

                // Check if start time and end time is within availability start and end time
                if (!appointmentStartTime.isBefore(availability.getStartTime()) &&
                    !appointmentStartTime.isAfter(appointmentEndTime) &&
                    !appointmentEndTime.isBefore(availability.getStartTime()) &&
                    !appointmentEndTime.isAfter(availability.getEndTime())) {

                    haveStartAndEndTimeAvailability = true;

//                    System.out.println(String.format("Start time and end time available: %s - %s", availability.getStartTime(), availability.getEndTime()));
//                    System.out.println(String.format("Start time and end time of appointment: %s - %s", appointmentStartTime, appointmentEndTime));

                    // Doctor is available if not yet booked
                    // Ensure that there are no other appointments clashing with the time
                    // Check if doctor is already booked

                    // Get doctor's appointments
                    Set<Appointment> doctorAppointments = doctor.getAppointments();
                    // Search any of the doctor appointments that fall on the same date as the new appointment
                    for (Appointment doctorAppointment : doctorAppointments) {
//                        System.out.println(String.format("Doctor's appointment: %s", doctorAppointment));

                        if (doctorAppointment.getDate().equals(appointmentDate)) {
//                            System.out.println(String.format("Date of doctor appointment: %s", doctorAppointment.getDate()));
                            // Check for a time clash

                            // If newAppointment start time is before appointment start time, then newAppointment
                            // end time must be before appointment start time
                            if (appointmentStartTime.isBefore(doctorAppointment.getStartTime()) &&
                                    appointmentEndTime.isBefore(doctorAppointment.getStartTime())) {
//                                System.out.println(String.format("Time range valid 1: %s - %s", appointmentStartTime, appointmentEndTime));
                                continue;
                            }
                            // Else If newAppointment start time is after appointment end time, then newAppointment
                            // end time must be after appointment end time
                            else if (appointmentStartTime.isAfter(doctorAppointment.getEndTime()) &&
                                    appointmentEndTime.isAfter(doctorAppointment.getEndTime())) {
//                                System.out.println(String.format("Time range valid 2: %s - %s", appointmentStartTime, appointmentEndTime));
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
//                            System.out.println(String.format("Date of patient appointment: %s", patientAppointment.getDate()));
                            // Check for a time clash

                            // If newAppointment start time is before appointment start time, then newAppointment
                            // end time must be before appointment start time
                            if (appointmentStartTime.isBefore(patientAppointment.getStartTime()) &&
                                    appointmentEndTime.isBefore(patientAppointment.getStartTime())) {
//                                System.out.println(String.format("Time range valid 1: %s - %s", appointmentStartTime, appointmentEndTime));
                                continue;
                            }
                            // Else If newAppointment start time is after appointment end time, then newAppointment
                            // end time must be after appointment end time
                            else if (appointmentStartTime.isAfter(patientAppointment.getEndTime()) &&
                                    appointmentEndTime.isAfter(patientAppointment.getEndTime())) {
//                                System.out.println(String.format("Time range valid 2: %s - %s", appointmentStartTime, appointmentEndTime));
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

        if (!haveStartAndEndTimeAvailability) {
            // Doctor is not available
            throw new DoctorUnavailableException(doctor.getUserID(), appointmentDayOfWeek, appointmentStartTime,
                    appointmentEndTime);
        }

        appointmentRepository.save(appointment);
        return appointment;
    }

    // Reschedule an existing appointment by ID
    public Appointment rescheduleAppointment(int appointmentID, Appointment updatedAppointment) throws
            InvalidTimeException, EntityTimeClashException, DoctorUnavailableException {
        Appointment oldAppointment = this.getAppointmentByID(appointmentID);

        // Store old appointment values to restore in case of failure
//        LocalDate oldAppointmentDate = oldAppointment.getDate();
//        LocalTime oldAppointmentStartTime = oldAppointment.getStartTime();
//        LocalTime oldAppointmentEndTime = oldAppointment.getEndTime();
//        String oldAppointmentStatus = oldAppointment.getAppointmentStatus();
//        Doctor oldDoctor = oldAppointment.getDoctor();
//        Patient oldPatient = oldAppointment.getPatient();


        // Need to store in separate variables because methods performed on oldAppointment changes the database, therefore
        // rewriting data in the database (not yet saved to database but makes it hard to work with)
        LocalDate updatedAppointmentDate = updatedAppointment.getDate() == null ?
                oldAppointment.getDate() : updatedAppointment.getDate();

        LocalTime updatedAppointmentStartTime = updatedAppointment.getStartTime() == null ?
                oldAppointment.getStartTime() : updatedAppointment.getStartTime();

        LocalTime updatedAppointmentEndTime = updatedAppointment.getEndTime() == null ?
                oldAppointment.getEndTime() : updatedAppointment.getEndTime();

        String updatedAppointmentStatus = updatedAppointment.getAppointmentStatus() == null ?
                oldAppointment.getAppointmentStatus() : updatedAppointment.getAppointmentStatus();

        Doctor updatedDoctor = updatedAppointment.getDoctor() == null || updatedAppointment.getDoctor().getUserID() == 0 ?
                oldAppointment.getDoctor() :
                doctorService.getDoctorByID(updatedAppointment.getDoctor().getUserID());

        Patient updatedPatient = updatedAppointment.getPatient() == null || updatedAppointment.getPatient().getUserID() == 0 ?
                oldAppointment.getPatient() :
                patientService.getPatientByID(updatedAppointment.getPatient().getUserID());

        // Ensure time range will still be valid after update
        if (updatedAppointmentStartTime.isAfter(updatedAppointmentEndTime)) {
            throw new InvalidTimeException(updatedAppointmentStartTime, updatedAppointmentEndTime);
        }

        // Updated appointments have to be within doctor's availabilities
        // Updated appointments must not clash with existing appointments
        // Ensure appointment is made at a valid date and time

//        System.out.println("NEW APPOINTMENT");

        Set<Availability> updatedDoctorAvailabilities = updatedDoctor.getAvailabilities();

        DayOfWeek updatedAppointmentDayOfWeek = updatedAppointmentDate.getDayOfWeek();

        boolean haveStartAndEndTimeAvailability = false;

        // Ensure appointment is made at a time when doctor is available
        // Check for a particular day of the week
        for (Availability availability : updatedDoctorAvailabilities) {
//            System.out.println(String.format("Doctor Availability Day of Week: %s", availability.getDayOfWeek()));
//            System.out.println(String.format("Doctor Availability Start Time: %s", availability.getStartTime()));
//            System.out.println(String.format("Doctor Availability End Time: %s", availability.getEndTime()));
//            System.out.println();


            if (availability.getDayOfWeek().equals(updatedAppointmentDayOfWeek)) {
//                System.out.println(String.format("There is a Day Of Week available: %s", updatedAppointmentDayOfWeek));

                // Check if start time and end time is within availability start and end time
                if (!updatedAppointmentStartTime.isBefore(availability.getStartTime()) &&
                        !updatedAppointmentStartTime.isAfter(updatedAppointmentEndTime) &&
                        !updatedAppointmentEndTime.isBefore(availability.getStartTime()) &&
                        !updatedAppointmentEndTime.isAfter(availability.getEndTime())) {

                    haveStartAndEndTimeAvailability = true;

//                    System.out.println(String.format("Start time and end time availability: %s - %s", availability.getStartTime(), availability.getEndTime()));
//                    System.out.println(String.format("Start time and end time of updated appointment: %s - %s", updatedAppointmentStartTime, updatedAppointmentEndTime));
//                    System.out.println();

                    // Doctor is available if not yet booked
                    // Ensure that there are no other appointments clashing with the time
                    // Check if doctor is already booked

                    // Get doctor's appointments
                    Set<Appointment> updatedDoctorAppointments = updatedDoctor.getAppointments();
                    // Search any of the doctor appointments that fall on the same date as the new appointment
                    for (Appointment doctorAppointment : updatedDoctorAppointments) {
//                        System.out.println(String.format("Doctor's appointment date: %s", doctorAppointment.getDate()));
//                        System.out.println(String.format("Doctor's appointment start time: %s", doctorAppointment.getStartTime()));
//                        System.out.println(String.format("Doctor's appointment end time: %s", doctorAppointment.getEndTime()));
//                        System.out.println();

                        // If it is the same appointment, no need to check since it's being updated
                        if (doctorAppointment.getAppointmentID() == appointmentID) {
                            continue;
                        }

                        if (doctorAppointment.getDate().equals(updatedAppointmentDate)) {
//                            System.out.println(String.format("Doctor has appointments on this date: %s", doctorAppointment.getDate()));
                            // Check for a time clash

                            // If newAppointment start time is before appointment start time, then newAppointment
                            // end time must be before appointment start time
                            if (updatedAppointmentStartTime.isBefore(doctorAppointment.getStartTime()) &&
                                    updatedAppointmentEndTime.isBefore(doctorAppointment.getStartTime())) {
//                                System.out.println(String.format("Time range valid 1: %s - %s", updatedAppointmentStartTime, updatedAppointmentEndTime));
                                continue;
                            }
                            // Else If newAppointment start time is after appointment end time, then newAppointment
                            // end time must be after appointment end time
                            else if (updatedAppointmentStartTime.isAfter(doctorAppointment.getEndTime()) &&
                                    updatedAppointmentEndTime.isAfter(doctorAppointment.getEndTime())) {
//                                System.out.println(String.format("Time range valid 2: %s - %s", updatedAppointmentStartTime, updatedAppointmentEndTime));
                                continue;
                            }
                            else {
                                // Throw exception here for doctor time clash
                                throw new EntityTimeClashException("Doctor", updatedDoctor.getUserID(),
                                        doctorAppointment.getStartTime(), doctorAppointment.getEndTime(),
                                        updatedAppointmentStartTime, updatedAppointmentEndTime);
                            }
                        }
                    }

                    // Check if patient has no other appointments during appointment time
                    Set<Appointment> patientAppointments = updatedPatient.getAppointments();

                    // Search any of the patient appointments that fall on the same date as the new appointment
                    for (Appointment patientAppointment : patientAppointments) {
//                        System.out.println("Patient appointment date: " + patientAppointment.getDate());
//                        System.out.println("Patient appointment start time: " + patientAppointment.getStartTime());
//                        System.out.println("Patient appointment end time: " + patientAppointment.getEndTime());
//                        System.out.println();

                        // If it is the same appointment, no need to check since it's being updated
                        if (patientAppointment.getAppointmentID() == appointmentID) {
                            continue;
                        }

                        if (patientAppointment.getDate().equals(updatedAppointmentDate)) {
//                            System.out.println(String.format("Patient has an appointment on this date: %s", patientAppointment.getDate()));

                            // Check for a time clash

                            // If newAppointment start time is before appointment start time, then newAppointment
                            // end time must be before appointment start time
                            if (updatedAppointmentStartTime.isBefore(patientAppointment.getStartTime()) &&
                                    updatedAppointmentEndTime.isBefore(patientAppointment.getStartTime())) {
//                                System.out.println(String.format("Time range valid 1: %s - %s", updatedAppointmentStartTime, updatedAppointmentEndTime));
                                continue;
                            }
                            // Else If newAppointment start time is after appointment end time, then newAppointment
                            // end time must be after appointment end time
                            else if (updatedAppointmentStartTime.isAfter(patientAppointment.getEndTime()) &&
                                    updatedAppointmentEndTime.isAfter(patientAppointment.getEndTime())) {
//                                System.out.println(String.format("Time range valid 2: %s - %s", updatedAppointmentStartTime, updatedAppointmentEndTime));
                                continue;
                            }
                            else {
                                // Throw exception here for patient time clash
                                throw new EntityTimeClashException("Patient", updatedPatient.getUserID(),
                                        patientAppointment.getStartTime(), patientAppointment.getEndTime(),
                                        updatedAppointmentStartTime, updatedAppointmentEndTime);
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

        if (!haveStartAndEndTimeAvailability) {
            // Doctor is not available
            throw new DoctorUnavailableException(updatedDoctor.getUserID(), updatedAppointmentDayOfWeek,
                    updatedAppointmentStartTime, updatedAppointmentEndTime);
        }

        oldAppointment.setDate(updatedAppointmentDate);
        oldAppointment.setStartTime(updatedAppointmentStartTime);
        oldAppointment.setEndTime(updatedAppointmentEndTime);
        oldAppointment.setAppointmentStatus(updatedAppointmentStatus);
        oldAppointment.setDoctor(updatedDoctor);
        oldAppointment.setPatient(updatedPatient);

        appointmentRepository.save(oldAppointment);

        return oldAppointment;
    }

    // Cancel an existing appointment by ID
    public Appointment cancelAppointment(int appointmentID) {
        Appointment deletedAppointment = this.getAppointmentByID(appointmentID);

        appointmentRepository.deleteById(appointmentID);

        return deletedAppointment;
    }


}
