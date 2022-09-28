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

    @Transactional
    // Get all doctors
    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAllBy(pageable);
    }

    @Transactional
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
        updatedDoctor.setFirstName(doctor.getFirstName());
        updatedDoctor.setLastName(doctor.getLastName());
        updatedDoctor.setEmail(doctor.getEmail());
        updatedDoctor.setPassword(doctor.getPassword());
        updatedDoctor.setDateOfBirth(doctor.getDateOfBirth());
        updatedDoctor.setSpecialty(doctor.getSpecialty());

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
