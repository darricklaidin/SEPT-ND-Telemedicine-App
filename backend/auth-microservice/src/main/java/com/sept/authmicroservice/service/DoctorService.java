package com.sept.authmicroservice.service;

import com.sept.authmicroservice.exception.ResourceAlreadyExistsException;
import com.sept.authmicroservice.exception.ResourceNotFoundException;
import com.sept.authmicroservice.model.Doctor;
import com.sept.authmicroservice.repository.DoctorRepository;
import com.sept.authmicroservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository, UserRepository userRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
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
    public Doctor updateDoctor(int doctorID, Doctor doctor) throws ResourceAlreadyExistsException {
        // Ensure doctor exists
        Doctor updatedDoctor = this.getDoctorByID(doctorID);

        updatedDoctor.setFirstName(doctor.getFirstName());
        updatedDoctor.setLastName(doctor.getLastName());

        if (!updatedDoctor.getEmail().equals(doctor.getEmail())) {
            if (Boolean.TRUE.equals(userRepository.existsByEmail(doctor.getEmail()))) {
                throw new ResourceAlreadyExistsException("User", "email", doctor.getEmail());
            }
            updatedDoctor.setEmail(doctor.getEmail());
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (doctor.getPassword() != null) {
            updatedDoctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        }

        updatedDoctor.setDateOfBirth(doctor.getDateOfBirth());
        updatedDoctor.setAccountStatus(doctor.isAccountStatus());
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
