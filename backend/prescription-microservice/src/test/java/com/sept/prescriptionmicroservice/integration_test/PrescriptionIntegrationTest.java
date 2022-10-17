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
        prescriptionRepository.deleteAll(); //clear database

        //Add new prescriptions
        Prescription prescription1 = new Prescription(1,2,1,"Panadol");

        Prescription prescription2 = new Prescription(2,4,3,"Telfast");

        Prescription prescription3 = new Prescription(3,2,3,"Zyrtec");

        Prescription prescription4 = new Prescription(3,2,1,"Tester");

        prescriptionRepository.save(prescription1);
        prescriptionRepository.save(prescription2);
        prescriptionRepository.save(prescription3);
        prescriptionRepository.save(prescription4);

        Prescription prescription = prescriptionRepository.findAllBy(null).getContent().get(0);

    }

    @Test
    void getPrescriptionByID() throws Exception {
        Prescription prescription = prescriptionRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(get("/api/prescriptions/" + prescription.getPrescriptionID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prescription").value(prescription.getPrescription()))
                .andExpect(jsonPath("$.patientID").value(1))
                .andExpect(jsonPath("$.doctorID").value(2));
    }


    @Test
    void testGetPrescriptionByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/prescriptions/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllPrescriptions() throws Exception {
        mockMvc.perform(get("/api/prescriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content']", hasSize(4)))
                .andExpect(jsonPath("$['content'][0].prescription").value("Panadol"))
                .andExpect(jsonPath("$['content'][1].prescription").value("Telfast"))
                .andExpect(jsonPath("$['content'][2].prescription").value("Zyrtec"))
                .andExpect(jsonPath("$['content'][3].prescription").value("Tester"));

    }


    @Test
    void getPatientPrescriptions() throws Exception {
        Prescription prescription = prescriptionRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(get("/api/prescriptions/patient/"+prescription.getPatientID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content']", hasSize(2)))
                .andExpect(jsonPath("$['content'][0].prescription").value("Panadol"))
                .andExpect(jsonPath("$['content'][1].prescription").value("Tester"));
    }

    @Test
    void getDoctorPrescriptions() throws Exception {
        Prescription prescription = prescriptionRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(get("/api/prescriptions/doctor/"+prescription.getDoctorID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content']", hasSize(3)))
                .andExpect(jsonPath("$['content'][0].prescription").value("Panadol"))
                .andExpect(jsonPath("$['content'][1].prescription").value("Zyrtec"))
                .andExpect(jsonPath("$['content'][2].prescription").value("Tester"));
    }

    @Test
    void createPrescription() throws Exception {
        mockMvc.perform(post("/api/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"prescription\": \"Cough Med\" }")
//                        .content("{ \"patientID\": 3 }")
//                        .content("{ \"doctorID\": 2 }")
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prescription").value("Cough Med"));
//                .andExpect(jsonPath("$.patientID").value("1"));
    }

    @Test
    void updatePrescription() throws Exception {
        Prescription prescription = prescriptionRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(put("/api/prescriptions/" + prescription.getPrescriptionID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"prescription\": \"Cough\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prescription").value("Cough"));
    }

    @Test
    void deletePrescription() throws Exception {
        Prescription prescription = prescriptionRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(delete("/api/prescriptions/" + prescription.getPrescriptionID()))
                .andExpect(status().isOk());

        assertTrue(prescriptionRepository.findByPrescriptionID(prescription.getPrescriptionID()).isEmpty());
    }
}
