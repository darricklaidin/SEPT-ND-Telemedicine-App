package com.sept.authmicroservice.controller;

import com.sept.authmicroservice.model.Patient;
import com.sept.authmicroservice.repository.PatientRepository;
import com.sept.authmicroservice.repository.UserRepository;
import com.sept.authmicroservice.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PatientControllerTest {

    private PatientService mockPatientService;

    private Patient patient1;
    private Patient patient2;
    private final List<Patient> patients = new ArrayList<>();

    LocalDate dob1;
    LocalDate dob2;

    @BeforeEach
    void setUp() {
        mockPatientService = Mockito.mock(PatientService.class);

        dob1 = LocalDate.of(2000, Month.JANUARY, 1);
        dob2 = LocalDate.of(2001, Month.FEBRUARY, 2);

        patient1 = new Patient(1, "Hirday", "Bajaj", "patient@fmail.com", "password", dob1, null);
        patient1.setSymptoms("Cough");
        patient2 = new Patient(2, "Mohamed", "Mahamed", "pc@fmail.coa", "password", dob2, null);
        patient2.setSymptoms("Stomachache");

        patients.add(patient1);
        patients.add(patient2);
    }

    @Test
    void getAllPatients() {
        when(mockPatientService.getAllPatients(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(patient1, patient2))));

        Page<Patient> patientPage = mockPatientService.getAllPatients(null);

        List<Patient> retrievedPatients = patientPage.getContent();

        // Test doctor list length
        assertEquals(2, patients.size());

        // Test doctor 1 match
        assertEquals(patient1, patients.get(0));

        // Test doctor 2 match
        assertEquals(patient2, patients.get(1));

        // Test each doctor matches
        for (int i = 0; i < patients.size(); i++) {
            assertEquals(patients.get(i), retrievedPatients.get(i));
        }
    }
    @Test
    void getPatientByID() {
    }

    @Test
    void updatePatient() {
    }

    @Test
    void deletePatient() {
    }
}