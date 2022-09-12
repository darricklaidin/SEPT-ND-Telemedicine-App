package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.exception.InvalidTimeException;
import org.superfive.telemedicine.exception.ResourceAlreadyExistsException;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.repository.AppointmentRepository;

import java.time.LocalTime;


@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
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
    public Appointment addAppointment(Appointment appointment) throws ResourceAlreadyExistsException, InvalidTimeException {
        // Ensure appointment ID does not already exist
        try {
            this.getAppointmentByID(appointment.getAppointmentID());
            throw new ResourceAlreadyExistsException("Appointment", "appointmentID", appointment.getAppointmentID());
        }
        catch(ResourceNotFoundException ex) {
            // appointment ID does not exist so new appointment can be added
        }

        // Ensure start time comes before end time
        LocalTime startTime = appointment.getStartTime();
        LocalTime endTime = appointment.getEndTime();

        if (startTime.isAfter(endTime)) {
            throw new InvalidTimeException(startTime, endTime);
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

        appointmentRepository.save(newAppointment);

        return newAppointment;
    }

    // Cancel an existing appointment by ID
    public Integer cancelAppointment(int appointmentID) {
        appointmentRepository.deleteById(appointmentID);

        return appointmentID;
    }


}
