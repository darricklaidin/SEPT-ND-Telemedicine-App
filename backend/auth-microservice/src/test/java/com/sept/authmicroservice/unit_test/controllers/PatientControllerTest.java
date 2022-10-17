package com.sept.authmicroservice.unit_test.controllers;

import com.sept.authmicroservice.controller.PatientController;
import com.sept.authmicroservice.model.Patient;
import com.sept.authmicroservice.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PatientControllerTest {

    private PatientService mockPatientService;
    private PatientController patientController;

    private Patient patient1;
    private Patient patient2;
    private final List<Patient> patients = new ArrayList<>();

    LocalDate dob1;
    LocalDate dob2;

    @BeforeEach
    void setUp() {
        mockPatientService = Mockito.mock(PatientService.class);
        patientController = new PatientController(mockPatientService);

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
        when(mockPatientService.getPatientByID(patient1.getUserID())).thenReturn(patient1);

        Patient test_patient = patientController.getPatientByID(patient1.getUserID()).getBody();

        assertEquals(patient1, test_patient);
    }

    @Test
    void updatePatient() {
        Patient updatedPatient = new Patient(patient1.getUserID(), "Darrick", "Laidin", "updatedl@gmail.com",
                "update", dob2 , null);
        updatedPatient.setAccountStatus(false);
        updatedPatient.setSymptoms("Blindness");

        Patient oldPatient1 = new Patient(patient1.getUserID(), patient1.getFirstName(), patient1.getLastName(),
                patient1.getEmail(), patient1.getPassword(), patient1.getDateOfBirth(), null);
        oldPatient1.setSymptoms(patient1.getSymptoms());

        when(mockPatientService.getPatientByID(patient1.getUserID())).thenReturn(patient1);
        when(mockPatientService.updatePatient(patient1.getUserID(),updatedPatient)).thenAnswer(i -> {
            patient1.setAccountStatus(updatedPatient.isEnabled());
            patient1.setFirstName(updatedPatient.getFirstName());
            patient1.setLastName(updatedPatient.getLastName());
            patient1.setEmail(updatedPatient.getEmail());
            patient1.setPassword(updatedPatient.getPassword());
            patient1.setDateOfBirth(updatedPatient.getDateOfBirth());
            patient1.setSymptoms(updatedPatient.getSymptoms());
            return patient1;
        });

        patientController.updatePatient(patient1.getUserID(), updatedPatient);

        assertEquals(updatedPatient.getFirstName(), patient1.getFirstName());
        assertEquals(updatedPatient.getLastName(), patient1.getLastName());
        assertEquals(updatedPatient.getEmail(), patient1.getEmail());
        assertEquals(updatedPatient.getPassword(), patient1.getPassword());
        assertEquals(updatedPatient.getDateOfBirth(), patient1.getDateOfBirth());
        assertEquals(updatedPatient.isEnabled(), patient1.isEnabled());
        assertEquals(updatedPatient.getSymptoms(), patient1.getSymptoms());

        assertNotEquals(oldPatient1.getFirstName(), patient1.getFirstName());
        assertNotEquals(oldPatient1.getLastName(), patient1.getLastName());
        assertNotEquals(oldPatient1.getEmail(), patient1.getEmail());
        assertNotEquals(oldPatient1.getPassword(), patient1.getPassword());
        assertNotEquals(oldPatient1.getDateOfBirth(), patient1.getDateOfBirth());
        assertNotEquals(oldPatient1.isEnabled(), patient1.isEnabled());
        assertNotEquals(oldPatient1.getSymptoms(), patient1.getSymptoms());

        assertNull(patient1.getRoles());
    }

    @Test
    void deletePatient() {
        when(mockPatientService.deletePatient(patient1.getUserID())).thenReturn(patient1); //return patient1

        Patient deletedPatient = patientController.deletePatient(patient1.getUserID()).getBody();

        when(mockPatientService.getAllPatients(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(patient2))));

        Page<Patient> patientPage = patientController.getAllPatients(null).getBody();

        if (patientPage == null) {
            fail("Patient page is null");
        }

        List<Patient> retrievedPatients = patientPage.getContent();

        // Test patient list length
        assertEquals(patients.size() - 1, retrievedPatients.size());

        // Test that patient1 has been deleted
        assertEquals(patient2, retrievedPatients.get(0));

        // Test that deleted patient matches patient1
        assertEquals(patient1, deletedPatient);
    }
}