package com.sept.authmicroservice.unit_test;

import com.sept.authmicroservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.BDDMockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import com.sept.authmicroservice.model.*;
import com.sept.authmicroservice.service.*;
import com.sept.authmicroservice.repository.PatientRepository;
import com.sept.authmicroservice.service.PatientService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PatientServiceUnitTest {
    private Patient patient;
    private Patient patient2;

    private UserRepository mockUserRepository;
    private PatientRepository mockPatientRepository;
    private PatientService patientService;
    private AuthService authService;

    LocalDate dob;
    LocalDate dob2;

    @BeforeEach
    void setUp() {
        mockPatientRepository = Mockito.mock(PatientRepository.class);
        mockUserRepository = Mockito.mock(UserRepository.class);
        patientService = new PatientService(mockPatientRepository, mockUserRepository);
        dob = LocalDate.of(2000, Month.APRIL, 18);
        dob2 = LocalDate.of(2001, Month.MAY, 19);
        patient = new Patient(1, "Hirday", "Bajaj", "patient@fmail.com", "password", dob, null);
        patient2 = new Patient(2, "Bryan", "Hong", "patient@fmail.com", "password", dob2, null);
    }

    @Test
    void getAlLPatient_SUCCESS() {
        when(mockPatientRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(patient, patient2 ))));
        Page<Patient> patientPage = patientService.getAllPatients(null);

        // Get patient page

        List<Patient> patients = patientPage.getContent();  // Get list of patients from patient page

        // Test patient list length
        assertEquals(2, patients.size());

        // Test patient 1 match
        assertEquals(patient, patients.get(0));

        // Test patient 2 match
        assertEquals(patient2, patients.get(1));
    }

    @Test
    void getPatientByID() {
        when(mockPatientRepository.findByUserID(patient.getUserID())).thenReturn(Optional.of(patient));

        Patient test_patient = patientService.getPatientByID(patient.getUserID());

        Assertions.assertTrue(test_patient.equals(patient));
    }

    @Test
    void updatePatient() {

        LocalDate dob3 = LocalDate.of(2002, Month.JULY, 16);

        BDDMockito.given(mockPatientRepository.findByUserID(patient.getUserID())).willReturn(Optional.of(patient));

        Patient updatePatient = new Patient(2, "Nimesh", "W", "update@gmail.com", "update", dob3 , null);

        patientService.updatePatient(patient.getUserID(), updatePatient);

        assertEquals(1, patient.getUserID());
        assertEquals("Nimesh", patient.getFirstName());
        assertEquals("W", patient.getLastName());
        assertEquals("update@gmail.com", patient.getEmail());
//        assertEquals("update", patient.getPassword());
        assertEquals(dob3,patient.getDateOfBirth());
        assertNull(patient.getRoles());

    }

    @Test
    void deletePatient() {
        when(mockPatientRepository.findByUserID(patient.getUserID())).thenReturn(Optional.of(patient));

        patientService.deletePatient(patient.getUserID());

        verify(mockPatientRepository).deleteById(patient.getUserID());
    }
}