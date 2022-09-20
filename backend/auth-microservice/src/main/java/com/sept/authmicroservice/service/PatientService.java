package com.sept.authmicroservice.service;

import com.sept.authmicroservice.exception.ResourceAlreadyExistsException;
import com.sept.authmicroservice.exception.ResourceNotFoundException;
import com.sept.authmicroservice.model.Patient;
import com.sept.authmicroservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Page<Patient> getAllPatients(Pageable pageable) {
        return patientRepository.findAllBy(pageable);
    }

    public Patient getPatientByID(int patientID) throws ResourceNotFoundException {
        return patientRepository.findByUserID(patientID)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "userID", patientID));
    }

    public Patient createPatient(Patient patient) throws ResourceAlreadyExistsException {
        // Ensure that patient id does not already exist
        try {
            this.getPatientByID(patient.getUserID());
            throw new ResourceAlreadyExistsException("Patient", "userID", patient.getUserID());
        } catch (ResourceNotFoundException exception) {
            // Patient does not already exist, continue...
        }

        patientRepository.save(patient);

        return patient;
    }

    public Patient deletePatient(int patientID) {
        Patient deletedPatient = this.getPatientByID(patientID);

        patientRepository.deleteById(patientID);

        return deletedPatient;
    }
}
