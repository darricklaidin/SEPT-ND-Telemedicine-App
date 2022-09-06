package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Patient;
import org.superfive.telemedicine.repository.PatientRepository;

import java.util.List;
import java.util.Set;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatientsByFilter(String accountStatus) {
        if (accountStatus == null) {
            return patientRepository.findAllBy();
        }
        return patientRepository.findAllByAccountStatus(accountStatus);
    }

    public Patient getPatientByID(int patientID) throws ResourceNotFoundException {
        return patientRepository.findByUserID(patientID)
                .orElseThrow(() -> new ResourceNotFoundException("Patient ID", "patientID", patientID));
    }

    public Set<Appointment> getPatientAppointments(int patientID) {
        return this.getPatientByID(patientID).getAppointments();
    }


}
