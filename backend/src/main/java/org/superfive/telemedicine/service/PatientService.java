package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Patient;
import org.superfive.telemedicine.repository.PatientRepository;
import org.superfive.telemedicine.utility.SortUtility;

import java.util.ArrayList;
import java.util.List;

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
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "patientID", patientID));
    }

    public List<Appointment> getPatientAppointments(int patientID, String sortMethod) {
        List<Appointment> appointments = new ArrayList<>(this.getPatientByID(patientID).getAppointments());
        SortUtility.sortAppointments(sortMethod, appointments);
        return appointments;
    }

}
