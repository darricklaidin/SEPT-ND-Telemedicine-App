package org.superfive.telemedicine;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.superfive.telemedicine.exception.ResourceAlreadyExistsException;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.*;
import org.superfive.telemedicine.service.*;
import org.superfive.telemedicine.repository.PatientRepository;
import org.superfive.telemedicine.service.PatientService;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class PatientServicesTest {

    private String accountStatus;
    private Set<Appointment> appointments;
    private Patient patient;
    private PatientRepository patientRepository;
    private PatientService patientService;
    LocalDate dob;

    @BeforeEach
    public void setup() {
        patientRepository = Mockito.mock(PatientRepository.class);
        patientService = new PatientService(patientRepository);
        accountStatus = "ACTIVE";
        LocalDate dob = LocalDate.of(2000, Month.APRIL, 18);
        patient = new Patient(2, "Hirday", "Bajaj", "patient@fmail.com", "password", dob, accountStatus, appointments);
    }

    @Test
    public void TestPatientByID_TRUE() throws ResourceNotFoundException {
        patientService.createPatient(patient);
        when(patientRepository.findByUserID(patient.getUserID())).thenReturn(Optional.of(patient));
        Patient test = patientService.getPatientByID(patient.getUserID());
        Assertions.assertTrue(test.equals(patient));
    }

    @Test
    public void TestPatientByID_THROWS() throws ResourceNotFoundException {
        patientService.createPatient(patient);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientByID(2));
    }

    @Test
    public void TestGetPatientAppointment() {
        when(patientRepository.findByUserID(patient.getUserID()).get().getAppointments());
        List<Appointment> test = patientService.getPatientAppointments(0, "Asc");
        Assertions.assertTrue(test.size() == 0);
    }

    @Test
    public void TestCreatePatient_TRUE() throws ResourceAlreadyExistsException{
        Patient test = patientService.createPatient(patient);
        Assertions.assertTrue(test.equals(patient));
    }

    @Test
    public void TestCreate2Patients_FALSE() throws ResourceAlreadyExistsException{
        patientService.createPatient(patient);
        when(patientRepository.findByUserID(patient.getUserID())).thenReturn(Optional.of(patient));
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> patientService.createPatient(patient));
    }

    @Test
    public void TestDeletePatient() {
        when(patientRepository.findByUserID(patient.getUserID())).thenReturn(Optional.of(patient));

        patientService.deletePatient(patient.getUserID());

        verify(patientRepository).deleteById(patient.getUserID());
    }

    @Test
    public void TestUpdatePatient() throws ResourceAlreadyExistsException{
        LocalDate dob2 = LocalDate.of(2002, Month.JULY, 16);
        Patient patient2 = new Patient(2, "Bryan", "Hong", "update@gmail.com", "update", dob2 , "ACTIVE", null);
        Patient patient3 = new Patient(2, "Mandy", "Tong", "update@gmail.com", "update", dob2 , "ACTIVE", null);
        BDDMockito.given(patientRepository.findByUserID(patient.getUserID())).willReturn(Optional.of(patient));

        patientService.updatePatient(patient.getUserID(), patient2);

        assertEquals("Bryan", patient.getFirstName());
        assertEquals(1, patient.getUserID());
    }
}
