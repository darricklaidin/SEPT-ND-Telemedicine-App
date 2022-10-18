package sept.superfive.prescriptionmicroservice.unit_test.controllers;

import sept.superfive.prescriptionmicroservice.controller.PrescriptionController;
import sept.superfive.prescriptionmicroservice.model.Prescription;
import sept.superfive.prescriptionmicroservice.payload.PrescriptionDTO;
import sept.superfive.prescriptionmicroservice.service.PrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

class PrescriptionControllerUnitTest {

    private PrescriptionController prescriptionController;
    private PrescriptionService mockPrescriptionService;

    private Prescription prescription;
    private Prescription prescription2;
    private Prescription prescription3;

    @BeforeEach
    void setUp() {
        mockPrescriptionService = Mockito.mock(PrescriptionService.class);
        prescriptionController = new PrescriptionController(mockPrescriptionService);

        // Create new specialties
        prescription = new Prescription(1,2,1,"Panadol");

        prescription2 = new Prescription(2,4,3,"Telfast");

        prescription3 = new Prescription(3,2,3,"Telfast");
    }

    @Test
    void getAllPrescriptions() {

        ArrayList<Prescription> prescriptions_test;

        when(mockPrescriptionService.getAllPrescriptions(null))
                .thenReturn(new PageImpl<>(prescriptions_test = new ArrayList<>(Arrays.asList(prescription, prescription2,prescription3))));

        Page<Prescription> prescriptionPage = prescriptionController.getAllPrescriptions(null).getBody();

        if (prescriptionPage == null) {
            fail("Prescription page is null");
        }

        List<Prescription> retrievedPrescriptions = prescriptionPage.getContent();

        // Test prescription list length
        assertEquals(prescriptions_test.size(), retrievedPrescriptions.size());

        // Test each prescription matches
        for (int i = 0; i < prescriptions_test.size(); i++) {
            assertEquals(prescriptions_test.get(i), retrievedPrescriptions.get(i));
        }
    }

    @Test
    void getPrescriptionByID() {
        when(mockPrescriptionService.getPrescriptionByID(prescription.getPrescriptionID())).thenReturn(prescription);

        // Test prescription matches
        assertEquals(prescriptionController.getPrescriptionByID(prescription.getPrescriptionID()).getBody(), prescription);

    }

    @Test
    void getPatientPrescriptions() {
        ArrayList<Prescription> prescriptions_test;
        when(mockPrescriptionService.getPatientPrescriptions(prescription.getPatientID(), null))
                .thenReturn(new PageImpl<>(prescriptions_test = new ArrayList<>(Arrays.asList(prescription, prescription3))));


        Page<Prescription> prescriptionPage = prescriptionController.getPatientPrescriptions(prescription.getPatientID(),null).getBody();

        if (prescriptionPage == null) {
            fail("Prescription page is null");
        }

        List<Prescription> retrievedPrescriptions = prescriptionPage.getContent();

        // Test prescription list length
        assertEquals(prescriptions_test.size(), retrievedPrescriptions.size());

        // Test each prescription matches
        for (int i = 0; i < prescriptions_test.size(); i++) {
            assertEquals(prescriptions_test.get(i), retrievedPrescriptions.get(i));
        }

    }

    @Test
    void getDoctorPrescriptions() {
        ArrayList<Prescription> prescriptions_test;
        when(mockPrescriptionService.getDoctorPrescriptions(prescription.getDoctorID(),null))
                .thenReturn(new PageImpl<>(prescriptions_test = new ArrayList<>(Collections.singletonList(prescription))));

        Page<Prescription> prescriptionPage = prescriptionController.getDoctorPrescriptions(prescription.getDoctorID(),null).getBody();

        if (prescriptionPage == null) {
            fail("Prescription page is null");
        }

        List<Prescription> retrievedPrescriptions = prescriptionPage.getContent();

        // Test prescription list length
        assertEquals(prescriptions_test.size(), retrievedPrescriptions.size());

        // Test each prescription matches
        for (int i = 0; i < prescriptions_test.size(); i++) {
            assertEquals(prescriptions_test.get(i), retrievedPrescriptions.get(i));
        }

    }

    @Test
    void createPrescription() {

        //Method variable only for testing
        ArrayList<Prescription> prescriptions_test;

        Prescription newPrescription = new Prescription(4,2,3,"Chocolate");
        when(mockPrescriptionService.createPrescription(newPrescription)).thenReturn(newPrescription);

        Prescription createdPrescription = prescriptionController.createPrescription(newPrescription).getBody();

        if (createdPrescription == null) {
            fail("Created prescription is null");
        }

        when(mockPrescriptionService.getPrescriptionByID(createdPrescription.getPrescriptionID())).thenReturn(createdPrescription);

        when(mockPrescriptionService.getAllPrescriptions(null))
                .thenReturn(new PageImpl<>(prescriptions_test = new ArrayList<>(Arrays.asList(prescription, prescription2, prescription3, createdPrescription))));

        Page<Prescription> prescriptionPage = prescriptionController.getAllPrescriptions(null).getBody();

        if (prescriptionPage == null) {
            fail("Prescription page is null");
        }

        List<Prescription> retrievedPrescriptions = prescriptionPage.getContent();

        // Test prescription list length
        assertEquals(prescriptions_test.size(), retrievedPrescriptions.size());

        // Test get newPrescription by ID
        assertEquals(createdPrescription, prescriptionController.getPrescriptionByID(createdPrescription.getPrescriptionID()).getBody());

        // Test newPrescription name matches
        assertEquals(newPrescription.getPrescription(), retrievedPrescriptions.get(prescriptions_test.size()-1).getPrescription()); //prescriptions_test.size()-1 because it is index position

    }

    @Test
    void updatePrescription() {

        PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
        prescriptionDTO.setPrescription("Chocolate");
        prescriptionDTO.setPatientID(1);
        prescriptionDTO.setDoctorID(1);

        Prescription updatePrescription = new Prescription(prescription.getPrescriptionID(), prescriptionDTO.getDoctorID(),prescriptionDTO.getPatientID(),prescriptionDTO.getPrescription());


        when(mockPrescriptionService.updatePrescription(updatePrescription.getPrescriptionID(), prescriptionDTO)).thenAnswer(i -> {
            prescription.setPrescription(updatePrescription.getPrescription());
            prescription.setDoctorID(updatePrescription.getDoctorID());
            prescription.setPatientID(updatePrescription.getPatientID());
            return prescription;
        });


        when(mockPrescriptionService.getPrescriptionByID(prescription.getPrescriptionID()))
                .thenReturn(updatePrescription);

        Prescription updatedPrescription = prescriptionController
                .updatePrescription(prescriptionDTO, prescription.getPrescriptionID()).getBody();

        when(mockPrescriptionService.getAllPrescriptions(null))
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
        when(mockPrescriptionService.getPrescriptionByID(prescription.getPrescriptionID())).thenReturn(prescription);

        when(mockPrescriptionService.deletePrescription(prescription.getPrescriptionID())).thenReturn(prescription);

        Prescription deletedPrescription = prescriptionController.deletePrescription(prescription.getPrescriptionID()).getBody();

        ArrayList<Prescription> prescriptions_test;
        when(mockPrescriptionService.getAllPrescriptions(null))
                .thenReturn(new PageImpl<>(prescriptions_test = new ArrayList<>(Collections.singletonList(prescription2))));

        Page<Prescription> prescriptionPage = prescriptionController.getAllPrescriptions(null).getBody();

        if (prescriptionPage == null) {
            fail("Prescription page is null");
        }

        List<Prescription> retrievedPrescriptions = prescriptionPage.getContent();

        // Test prescription list length
        assertEquals(prescriptions_test.size(), retrievedPrescriptions.size());

        // Test that prescription has been deleted
        assertEquals(prescription2, retrievedPrescriptions.get(0));

        // Test that deleted prescription matches prescription
        assertEquals(prescription, deletedPrescription);
    }
}