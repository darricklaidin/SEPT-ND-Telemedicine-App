package com.sept.authmicroservice.service;

import com.sept.authmicroservice.exception.ResourceNotFoundException;
import com.sept.authmicroservice.model.Doctor;
import com.sept.authmicroservice.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    // Update an existing doctor
    public Doctor updateDoctor(int doctorID, Doctor doctor) {
        // Ensure doctor exists
        Doctor updatedDoctor = this.getDoctorByID(doctorID);

        if (doctor.getFirstName() != null) {
            updatedDoctor.setFirstName(doctor.getFirstName());
        }

        if (doctor.getLastName() != null) {
            updatedDoctor.setLastName(doctor.getLastName());
        }

        if (doctor.getEmail() != null) {
            updatedDoctor.setEmail(doctor.getEmail());
        }

        if (doctor.getPassword() != null) {
            updatedDoctor.setPassword(doctor.getPassword());
        }

        if (doctor.getDateOfBirth() != null) {
            updatedDoctor.setDateOfBirth(doctor.getDateOfBirth());
        }

        if (doctor.getSpecialty() != null) {
            updatedDoctor.setSpecialty(doctor.getSpecialty());
        }

        doctorRepository.save(updatedDoctor);

        return updatedDoctor;
    }

    // Delete an existing doctor
    public Doctor deleteDoctor(int doctorID) {
        Doctor deletedDoctor = this.getDoctorByID(doctorID);

        doctorRepository.deleteById(doctorID);

        return deletedDoctor;
    }
}
