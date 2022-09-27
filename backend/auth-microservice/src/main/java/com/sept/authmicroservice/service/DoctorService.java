package com.sept.authmicroservice.service;

import com.sept.authmicroservice.exception.ResourceNotFoundException;
import com.sept.authmicroservice.model.Doctor;
import com.sept.authmicroservice.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Get all doctors
    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAllBy(pageable);
    }

    // Get a doctor by ID
    public Doctor getDoctorByID(int doctorID) throws ResourceNotFoundException {
        return doctorRepository.findByUserID(doctorID)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "userID", doctorID));
    }

    @Transactional
    // Update an existing doctor
    public Doctor updateDoctor(int doctorID, Doctor doctor) {
        // Ensure doctor exists
        Doctor updatedDoctor = this.getDoctorByID(doctorID);
        doctorRepository.save(updatedDoctor);
        return updatedDoctor;
    }

    @Transactional
    // Delete an existing doctor
    public Doctor deleteDoctor(int doctorID) {
        Doctor deletedDoctor = this.getDoctorByID(doctorID);
        doctorRepository.deleteById(doctorID);
        return deletedDoctor;
    }
}
