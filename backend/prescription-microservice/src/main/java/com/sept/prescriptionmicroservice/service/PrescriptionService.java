package com.sept.prescriptionmicroservice.service;

import com.sept.prescriptionmicroservice.exception.ResourceAlreadyExistsException;
import com.sept.prescriptionmicroservice.exception.ResourceNotFoundException;
import com.sept.prescriptionmicroservice.model.Prescription;
import com.sept.prescriptionmicroservice.payload.PrescriptionDTO;
import com.sept.prescriptionmicroservice.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

@Service
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;

    private static final String RESOURCE_NAME = "Prescription";

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Transactional
    // Get an prescription by ID
    public Prescription getPrescriptionByID(int prescriptionID) throws ResourceNotFoundException {
        return prescriptionRepository.findByPrescriptionID(prescriptionID)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", "prescriptionID", prescriptionID));
    }

    @Transactional
    // Get all prescriptions
    public Page<Prescription> getAllPrescriptions(Pageable pageable) {
        return prescriptionRepository.findAllBy(pageable);
    }

    @Transactional
    // Get a patient's prescriptions
    public Page<Prescription> getPatientPrescriptions(int patientID, Pageable pageable) {
        return prescriptionRepository.findByPatientID(patientID, pageable);
    }

    @Transactional
    // Get a doctor's prescriptions
    public Page<Prescription> getDoctorPrescriptions(int doctorID, Pageable pageable) {
        return prescriptionRepository.findByDoctorID(doctorID, pageable);
    }


    @Transactional
    public Prescription createPrescription(Prescription prescription) throws ResourceAlreadyExistsException {
        // Ensure prescription ID does not already exist
        try {
            this.getPrescriptionByID(prescription.getPrescriptionID());
            throw new ResourceAlreadyExistsException(RESOURCE_NAME, "prescriptionID", prescription.getPrescriptionID());
        } catch (ResourceNotFoundException exception) {
            // Specialty name does not exist, continue...
        }
        Prescription temp = new Prescription(
                prescription.getPrescriptionID(),
                prescription.getDoctorID(),
                prescription.getPatientID(),
                prescription.getPrescription()
        );

        prescriptionRepository.save(temp);
        return temp;
    }

    @Transactional
    public Prescription updatePrescription(int prescriptionID, PrescriptionDTO prescription) {

        Prescription updatedPrescription = this.getPrescriptionByID(prescriptionID); //Also checks if specialtyID exists

        updatedPrescription.setPrescription(prescription.getPrescription());
        updatedPrescription.setDoctorID(prescription.getDoctorID());
        updatedPrescription.setPatientID(prescription.getPatientID());

        prescriptionRepository.save(updatedPrescription);

        return updatedPrescription;
    }

    @Transactional
    public Prescription deletePrescription(int prescriptionID) {
        Prescription deletedPrescription = this.getPrescriptionByID(prescriptionID);
        prescriptionRepository.deleteById(prescriptionID);
        return deletedPrescription;
    }
}

