package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Doctor;
import org.superfive.telemedicine.repository.AppointmentRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // Get all appointments
    public Set<Appointment> getAllAppointments() {
        return appointmentRepository.findAllBy();
    }

    // Get an appointment by ID
    public Appointment getAppointmentByID(int appointmentID) throws ResourceNotFoundException {
        return appointmentRepository.findByAppointmentID(appointmentID)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment ID", "appointmentID", appointmentID));
    }

    // Get all appointments by date
//    public List<Appointment> getAppointmentsByDate(String date) {
//        return appointmentRepository.findAllByDate(date);
//    }

    // Get all appointments by time

    // Get all appointments by status
    public Set<Appointment> getAllAppointmentsByStatus(String appointmentStatus) {
        return appointmentRepository.findByAppointmentStatus(appointmentStatus);
    }

    // Get an appointment's status
    public String getAppointmentStatus(int appointmentID) {
        return this.getAppointmentByID(appointmentID).getAppointmentStatus();
    }

    // Get an appointment's participants
    public Map<String, Integer> getAppointmentParticipants(int appointmentID) {
        HashMap<String, Integer> participantsMap = new HashMap<>();
        Appointment appointment = this.getAppointmentByID(appointmentID);
        participantsMap.put("doctorID", appointment.getDoctor().getUserID());
        participantsMap.put("patientID", appointment.getPatient().getUserID());
        return participantsMap;
    }



    // Add a new appointment

    // Reschedule an existing appointment by ID

    // Cancel an existing appointment by ID
}
