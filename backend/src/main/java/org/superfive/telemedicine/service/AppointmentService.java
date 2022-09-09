package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.exception.IDAlreadyExistsException;
import org.superfive.telemedicine.exception.InvalidScheduleException;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.repository.AppointmentRepository;

import java.time.LocalDateTime;


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
    public Appointment addAppointment(Appointment appointment) throws IDAlreadyExistsException, InvalidScheduleException {
        // Ensure appointment ID does not already exist
        try {
            this.getAppointmentByID(appointment.getAppointmentID());
            throw new IDAlreadyExistsException(appointment.getAppointmentID());
        }
        catch(ResourceNotFoundException ex) {
            // appointment ID does not exist so new appointment can be added
        }

        // Ensure start date time comes before end date time
        LocalDateTime startDateTime = appointment.getStartDateTime();
        LocalDateTime endDateTime = appointment.getEndDateTime();

        if (startDateTime.isAfter(endDateTime)) {
            throw new InvalidScheduleException(startDateTime, endDateTime);
        }

        appointmentRepository.save(appointment);
        return appointment;
    }

    // Reschedule an existing appointment by ID
    public Appointment rescheduleAppointmentByID(int appointmentID, Appointment updatedAppointment) throws
            InvalidScheduleException {
        Appointment newAppointment = this.getAppointmentByID(appointmentID);

        // Update attributes

        // Ensure start date time comes before end date time
        LocalDateTime startDateTime = updatedAppointment.getStartDateTime();
        LocalDateTime endDateTime = updatedAppointment.getEndDateTime();

        if (startDateTime.isAfter(endDateTime)) {
            throw new InvalidScheduleException(startDateTime, endDateTime);
        }

        newAppointment.setStartDateTime(updatedAppointment.getStartDateTime());
        newAppointment.setEndDateTime(updatedAppointment.getEndDateTime());
        newAppointment.setAppointmentStatus(updatedAppointment.getAppointmentStatus());
        newAppointment.setDoctor(updatedAppointment.getDoctor());
        newAppointment.setPatient(updatedAppointment.getPatient());

        appointmentRepository.save(newAppointment);

        return newAppointment;
    }

    // Cancel an existing appointment by ID
    public Integer cancelAppointmentByID(int appointmentID) {
        appointmentRepository.deleteById(appointmentID);

        return appointmentID;
    }


}
