package com.sept.authmicroservice.rename_as_required;

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

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PatientServiceTest {
    private Patient patient;

    private UserRepository userRepository;
    private PatientRepository patientRepository;
    private PatientService patientService;
    private AuthService authService;
    LocalDate dob;

    @BeforeEach
    void setUp() {
        patientRepository = Mockito.mock(PatientRepository.class);
        patientService = new PatientService(patientRepository, userRepository);
        LocalDate dob = LocalDate.of(2000, Month.APRIL, 18);
        patient = new Patient(1, "Hirday", "Bajaj", "patient@fmail.com", "password", dob, null);
    }

//    @Test
//    void getAllPatients() {
//    }

    @Test
    void getPatientByID() {
        when(patientRepository.findByUserID(patient.getUserID())).thenReturn(Optional.of(patient));

        Patient test_patient = patientService.getPatientByID(patient.getUserID());

        Assertions.assertTrue(test_patient.equals(patient));
    }

    @Test
    void updatePatient() {
        LocalDate dob2 = LocalDate.of(2002, Month.JULY, 16);
        Patient patient2 = new Patient(2, "Bryan", "Hong", "update@gmail.com", "update", dob2 , null);
        Patient patient3 = new Patient(2, "Mandy", "Tong", "update@gmail.com", "update", dob2, null);
        BDDMockito.given(patientRepository.findByUserID(patient.getUserID())).willReturn(Optional.of(patient));

        patientService.updatePatient(patient.getUserID(), patient2);

        assertEquals(1, patient.getUserID());
        assertEquals("Bryan", patient.getFirstName());
        assertEquals("Hong", patient.getLastName());
        assertEquals("update@gmail.com", patient.getEmail());
        assertEquals("update", patient.getPassword());
        assertEquals(dob2,patient.getDateOfBirth());
        assertNull(patient.getRoles());

        patientService.updatePatient(patient.getUserID(), patient3);

        assertEquals(1, patient.getUserID());
        assertEquals("Mandy", patient.getFirstName());
        assertEquals("Tong", patient.getLastName());
        assertEquals("update@gmail.com", patient.getEmail());
        assertEquals("update", patient.getPassword());
        assertEquals(dob2,patient.getDateOfBirth());
        assertNull(patient.getRoles());
    }

    @Test
    void deletePatient() {
        when(patientRepository.findByUserID(patient.getUserID())).thenReturn(Optional.of(patient));

        patientService.deletePatient(patient.getUserID());

        verify(patientRepository).deleteById(patient.getUserID());
    }
}