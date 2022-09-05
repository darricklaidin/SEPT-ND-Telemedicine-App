package org.superfive.telemedicine.service;

import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Doctor;
import org.superfive.telemedicine.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAllBy();
    }

    // Get a doctor by ID
    public Doctor getDoctorById(int doctorID) throws ResourceNotFoundException {
        return doctorRepository.findById(doctorID)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor ID", "doctorID", doctorID));
    }


}
