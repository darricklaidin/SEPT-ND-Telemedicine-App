package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Doctor;
import org.superfive.telemedicine.repository.DoctorRepository;
import org.superfive.telemedicine.utility.comparator.appointment.SortAppointment;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Get all doctors
    public Page<Doctor> getAllDoctorsByFilter(Pageable pageable) {
        return doctorRepository.findAllBy(pageable);
    }

    // Get a doctor by ID
    public Doctor getDoctorByID(int doctorID) throws ResourceNotFoundException {
        return doctorRepository.findByUserID(doctorID)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "doctorID", doctorID));
    }

    // Get a doctor's appointments
    public List<Appointment> getDoctorAppointments(int doctorID, String sortMethod) {
        List<Appointment> appointments = new ArrayList<>(this.getDoctorByID(doctorID).getAppointments());
        SortAppointment.sortAppointment(sortMethod, appointments);
        return appointments;
    }

}
