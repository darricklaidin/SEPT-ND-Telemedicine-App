package com.sept.prescriptionmicroservice.unit_test.services;

import com.sept.prescriptionmicroservice.model.Prescription;
import com.sept.prescriptionmicroservice.payload.PrescriptionDTO;
import com.sept.prescriptionmicroservice.repository.PrescriptionRepository;
import com.sept.prescriptionmicroservice.service.PrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class PrescriptionServiceUnitTest {

    private PrescriptionService prescriptionService;
    private PrescriptionRepository mockPrescriptionRepository;

    private Prescription prescription;
    private Prescription prescription2;

    private final List<Prescription> prescriptions = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mockPrescriptionRepository = Mockito.mock(PrescriptionRepository.class);
        prescriptionService = new PrescriptionService(mockPrescriptionRepository);

        // Create new specialties
        prescription = new Prescription(1,1,1,"Panadol");

        prescription2 = new Prescription(2,2,2,"Telfast");

        // Add specialties to specialties list
        prescriptions.add(prescription);
        prescriptions.add(prescription2);
    }

    @Test
    void getAllPrescriptions() {
        when(mockPrescriptionRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(prescription, prescription2))));

        Page<Prescription> prescriptionPage = prescriptionService.getAllPrescriptions(null);
        List<Prescription> retrievedPrescriptions = prescriptionPage.getContent();

        // Test prescription list length
        assertEquals(prescriptions.size(), retrievedPrescriptions.size());

        // Test each prescription matches
        for (int i = 0; i < prescriptions.size(); i++) {
            assertEquals(prescriptions.get(i), retrievedPrescriptions.get(i));
        }
    }


    @Test
    void getPrescriptionByID() {
        when(mockPrescriptionRepository.findByPrescriptionID(prescription.getPrescriptionID())).thenReturn(Optional.of(prescription));

        // Test prescription matches
        assertEquals(prescriptionService.getPrescriptionByID(prescription.getPrescriptionID()), prescription);

    }

    @Test
    void getPatientPrescriptions() {

    }

    @Test
    void getDoctorPrescriptions() {
    }

    @Test
    void createPrescription() {


        Prescription newPrescription = new Prescription(3,1,1,"Chocolate");
        when(mockPrescriptionRepository.save(newPrescription)).thenReturn(newPrescription);

        Prescription createdPrescription = prescriptionService.createPrescription(newPrescription);

        when(mockPrescriptionRepository.findByPrescriptionID(createdPrescription.getPrescriptionID()))
                .thenReturn(Optional.of(createdPrescription));

        when(mockPrescriptionRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(prescription, prescription, createdPrescription))));

        Page<Prescription> prescriptionPage = prescriptionService.getAllPrescriptions(null);
        List<Prescription> retrievedPrescriptions = prescriptionPage.getContent();

        // Test prescription list length
        assertEquals(prescriptions.size() + 1, retrievedPrescriptions.size());

        // Test get newPrescription by ID
        assertEquals(createdPrescription, prescriptionService.getPrescriptionByID(createdPrescription.getPrescriptionID()));

        // Test newPrescription name matches
        assertEquals(newPrescription.getPrescription(), retrievedPrescriptions.get(prescriptions.size()).getPrescription());

    }

    @Test
    void updatePrescription() {

        PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
        prescriptionDTO.setPrescription("Chocolate");
        prescriptionDTO.setPatientID(1);
        prescriptionDTO.setDoctorID(1);

        Prescription updatePrescription = new Prescription(1,prescriptionDTO.getDoctorID(),prescriptionDTO.getPatientID(),prescriptionDTO.getPrescription());


        when(mockPrescriptionRepository.save(updatePrescription)).thenAnswer(i -> {
            prescription.setPrescription(updatePrescription.getPrescription());
            prescription.setDoctorID(updatePrescription.getDoctorID());
            prescription.setPatientID(updatePrescription.getPatientID());
            return prescription;
        });


        when(mockPrescriptionRepository.findByPrescriptionID(prescription.getPrescriptionID()))
                .thenReturn(Optional.of(updatePrescription));

        Prescription updatedPrescription = prescriptionService.updatePrescription(prescription.getPrescriptionID(), prescriptionDTO);

        when(mockPrescriptionRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(prescription, prescription2))));

        // Test that specialty1's name has been updated
        assertEquals("Chocolate", prescription.getPrescription());

        // Test that the id of specialty 1 remains unchanged
        assertEquals(1, prescription.getPrescriptionID());

        // Test that updatedSpecialty matches with specialty1
        assertEquals(prescription, updatedPrescription);


    }

    @Test
    void deletePrescription() {
        when(mockPrescriptionRepository.findByPrescriptionID(prescription.getPrescriptionID())).thenReturn(Optional.of(prescription));

        doNothing().when(mockPrescriptionRepository).deleteById(prescription.getPrescriptionID());

        Prescription deletedPrescription = prescriptionService.deletePrescription(prescription.getPrescriptionID());

        when(mockPrescriptionRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(prescription2))));

        Page<Prescription> prescriptionPage = prescriptionService.getAllPrescriptions(null);
        List<Prescription> retrievedPrescriptions = prescriptionPage.getContent();

        // Test prescription list length
        assertEquals(prescriptions.size() - 1, retrievedPrescriptions.size());

        // Test that prescription has been deleted
        assertEquals(prescription2, retrievedPrescriptions.get(0));

        // Test that deleted prescription matches prescription
        assertEquals(prescription, deletedPrescription);
    }
}