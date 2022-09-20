package com.sept.authmicroservice.service;

import com.sept.authmicroservice.payload.DoctorSignUp;
import com.sept.authmicroservice.repository.DoctorRepository;
import com.sept.authmicroservice.repository.PatientRepository;
import com.sept.authmicroservice.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.RepeatedTest;
import com.sept.authmicroservice.exception.ResourceAlreadyExistsException;
import com.sept.authmicroservice.exception.ResourceNotFoundException;
import com.sept.authmicroservice.model.*;
import com.sept.authmicroservice.service.*;
import org.springframework.data.domain.Page;


import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DoctorServiceTest {

    private Doctor doctor;
    private Doctor doctor2;
    private DoctorRepository doctorRepository;
    private DoctorService doctorService;
    private AuthService authService;
    LocalDate dob;

    @BeforeEach
    void setUp() {
        doctorRepository = Mockito.mock(DoctorRepository.class);
        doctorService = new DoctorService(doctorRepository);
        LocalDate dob = LocalDate.of(2000, Month.APRIL, 18);


        doctor = new Doctor(1, "Hirday", "Bajaj", "patient@fmail.com", "password", dob, null , null);
    }

    //public DoctorSignUp(String firstName, String lastName, String email, String password,
    //            String dateOfBirth, Integer specialtyId)

//    @Test
//    void getAllDoctors() {
//        Page<Doctor> doctorPage = doctorService.getAllDoctors(null);  // get doctor page
//        List<Doctor> doctors = doctorPage.getContent();  // get list of doctors
//        // do test
//
//    }

    @Test
    void getDoctorByID() {

        when(doctorRepository.findByUserID(doctor.getUserID())).thenReturn(Optional.of(doctor));

        Doctor test_doctor = doctorService.getDoctorByID(doctor.getUserID());

        Assertions.assertTrue(test_doctor.equals(doctor));

    }

    //public Doctor(int userID, String firstName, String lastName, String email, String password,
    //                  LocalDate dateOfBirth, List<Role> roles, Specialty specialty

    @Test
    void updateDoctor() {

        LocalDate dob2 = LocalDate.of(2002, Month.JULY, 16);
        Doctor doctor2 = new Doctor(2, "Darrick", "Hong", "update@gmail.com", "update", dob2 , null, null);
        Doctor doctor3 = new Doctor(2, "Nim", "Tong", "update@gmail.com", "update", dob2, null,null);

        BDDMockito.given(doctorRepository.findByUserID(doctor.getUserID())).willReturn(Optional.of(doctor)); //1

        doctorService.updateDoctor(doctor.getUserID(), doctor2);

        assertEquals(1, doctor.getUserID());
        assertEquals("Darrick", doctor.getFirstName());
        assertEquals("Hong", doctor.getLastName());
        assertEquals("update@gmail.com", doctor.getEmail());
        assertEquals("update", doctor.getPassword());
        assertEquals(dob2,doctor.getDateOfBirth());
        assertEquals(null, doctor.getRoles());
        assertEquals(null, doctor.getSpecialty());

        doctorService.updateDoctor(doctor.getUserID(), doctor3);

        assertEquals(1, doctor.getUserID());
        assertEquals("Nim", doctor.getFirstName());
        assertEquals("Tong", doctor.getLastName());
        assertEquals("update@gmail.com", doctor.getEmail());
        assertEquals("update", doctor.getPassword());
        assertEquals(dob2,doctor.getDateOfBirth());
        assertEquals(null, doctor.getRoles());
        assertEquals(null, doctor.getSpecialty());
    }

    @Test
    void deleteDoctor() {
        when(doctorRepository.findByUserID(doctor.getUserID())).thenReturn(Optional.of(doctor));

        doctorService.deleteDoctor(doctor.getUserID());

        verify(doctorRepository).deleteById(doctor.getUserID());
    }
}