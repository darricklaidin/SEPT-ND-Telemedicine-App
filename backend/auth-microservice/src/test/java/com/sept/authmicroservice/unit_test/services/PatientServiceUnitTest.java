package com.sept.authmicroservice.unit_test.services;

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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class PatientServiceUnitTest {

    private PatientRepository mockPatientRepository;
    private PatientService patientService;

    private Patient patient1;
    private Patient patient2;
    private final List<Patient> patients = new ArrayList<>();

    LocalDate dob1;
    LocalDate dob2;


    @BeforeEach
    void setUp() {
        mockPatientRepository = Mockito.mock(PatientRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        patientService = new PatientService(mockPatientRepository, mockUserRepository);

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
        when(mockPatientRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(patient1, patient2))));

        Page<Patient> patientPage = patientService.getAllPatients(null);

        List<Patient> retrievedPatients = patientPage.getContent();

        // Test patient list length
        assertEquals(2, patients.size());

        // Test each patient matches
        for (int i = 0; i < patients.size(); i++) {
            assertEquals(patients.get(i), retrievedPatients.get(i));
        }
    }

    @Test
    void getPatientByID() {
        when(mockPatientRepository.findByUserID(patient1.getUserID())).thenReturn(Optional.of(patient1));
        assertEquals(patient1, patientService.getPatientByID(patient1.getUserID()));
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

        when(mockPatientRepository.findByUserID(patient1.getUserID())).thenReturn(Optional.of(patient1));
        when(mockPatientRepository.save(patient1)).thenAnswer(i -> {
            patient1.setAccountStatus(updatedPatient.isEnabled());
            patient1.setFirstName(updatedPatient.getFirstName());
            patient1.setLastName(updatedPatient.getLastName());
            patient1.setEmail(updatedPatient.getEmail());
            patient1.setPassword(updatedPatient.getPassword());
            patient1.setDateOfBirth(updatedPatient.getDateOfBirth());
            patient1.setSymptoms(updatedPatient.getSymptoms());
            return patient1;
        });

        patientService.updatePatient(patient1.getUserID(), updatedPatient);

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
        when(mockPatientRepository.findByUserID(patient1.getUserID())).thenReturn(Optional.of(patient1));

        doNothing().when(mockPatientRepository).deleteById(patient1.getUserID());

        Patient deletedPatient = patientService.deletePatient(patient1.getUserID());

        when(mockPatientRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(patient2))));

        Page<Patient> patientPage = patientService.getAllPatients(null);
        List<Patient> retrievedPatients = patientPage.getContent();

        // Test patient list length
        assertEquals(patients.size() - 1, retrievedPatients.size());

        // Test that patient1 has been deleted
        assertEquals(patient2, retrievedPatients.get(0));

        // Test that deleted patient matches patient1
        assertEquals(patient1, deletedPatient);
    }
}