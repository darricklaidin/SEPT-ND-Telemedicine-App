package com.sept.authmicroservice.service;

import com.sept.authmicroservice.exception.ResourceNotFoundException;
import com.sept.authmicroservice.exception.ResourceAlreadyExistsException;
import com.sept.authmicroservice.model.Patient;
import com.sept.authmicroservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Patient updatePatient(int patientID, Patient patient) throws ResourceAlreadyExistsException {
        // Check that patient exists
        Patient oldPatient = this.getPatientByID(patientID);

        if (patient.getFirstName() != null) {
            oldPatient.setFirstName(patient.getFirstName());
        }

        if (patient.getLastName() != null) {
            oldPatient.setLastName(patient.getLastName());
        }

        if (patient.getEmail() != null) {
            oldPatient.setEmail(patient.getEmail());
        }

        if (patient.getPassword() != null) {
            oldPatient.setPassword(patient.getPassword());
        }

        if (patient.getDateOfBirth() != null) {
            oldPatient.setDateOfBirth(patient.getDateOfBirth());
        }

        if (patient.getRoles() != null) {
            oldPatient.setRoles(patient.getRoles());
        }

        oldPatient.setSymptoms(patient.getSymptoms());

        patientRepository.save(oldPatient);

        return oldPatient;
    }
    public Patient deletePatient(int patientID) {
        Patient deletedPatient = this.getPatientByID(patientID);

        patientRepository.deleteById(patientID);

        return deletedPatient;
    }
}
