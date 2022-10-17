package com.sept.prescriptionmicroservice.integration_test;


import com.sept.prescriptionmicroservice.model.Prescription;
import com.sept.prescriptionmicroservice.payload.PrescriptionDTO;
import com.sept.prescriptionmicroservice.repository.PrescriptionRepository;
import com.sept.prescriptionmicroservice.service.PrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PrescriptionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionService prescriptionService;

    @BeforeEach
    void setUp() {
//        prescriptionRepository.deleteAll(); //clear database
//
//        //Add new prescriptions
//        Prescription prescription1 = new Prescription(1,1,1,"Panadol");
//
//        Prescription prescription2 = new Prescription(2,2,2,"Telfast");
//
//        Prescription prescription3 = new Prescription(3,2,1,"Zyrtec");
//
//        Prescription prescription = prescriptionRepository.findAllBy(null).getContent().get(0);
//
//        prescriptionService.createPrescription("")
    }

    @Test
    void getPrescriptionByID() {
    }

    @Test
    void getAllPrescriptions() {
    }

    @Test
    void getPatientPrescriptions() {
    }

    @Test
    void getDoctorPrescriptions() {
    }

    @Test
    void createPrescription() {
    }

    @Test
    void updatePrescription() {
    }

    @Test
    void deletePrescription() {
    }
}
