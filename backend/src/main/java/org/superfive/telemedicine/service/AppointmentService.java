package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.repository.AppointmentRepository;

import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAllBy();
    }

    // Get an appointment by ID
    public Appointment getAppointmentByID(int appointmentID) throws ResourceNotFoundException {
        return appointmentRepository.findById(appointmentID)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment ID", "appointmentID", appointmentID));
    }

    // Get appointments by date
//    public List<Appointment> getAppointmentsByDate(String date) {
//        return appointmentRepository.findAllByDate(date);
//    }

    // Get appointments by time

    // Get appointments where a specific doctor is a participant
    public List<Appointment> getAppointmentsByDoctorID(int doctorID) {
        return appointmentRepository.findByDoctorID(doctorID);
    }

    // Get appointments where a specific patient is a participant
    public List<Appointment> getAppointmentsByPatientID(int patientID) {
        return appointmentRepository.findByPatientID(patientID);
    }

    // Add a new appointment

    // Reschedule an existing appointment by ID

    // Cancel an existing appointment by ID
}
