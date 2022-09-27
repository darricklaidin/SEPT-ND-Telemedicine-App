package com.sept.authmicroservice.unit_test;

import com.sept.authmicroservice.repository.DoctorRepository;
import com.sept.authmicroservice.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.mockito.BDDMockito;
import org.junit.jupiter.api.BeforeEach;
import com.sept.authmicroservice.model.*;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;


import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DoctorUnitTest {

    private DoctorRepository mockDoctorRepository;
    private DoctorService doctorService;

    private Doctor doctor;
    private Doctor doctor2;

    @BeforeEach
    void setUp() {
        mockDoctorRepository = Mockito.mock(DoctorRepository.class);
        doctorService = new DoctorService(mockDoctorRepository);

        LocalDate dob = LocalDate.of(2000, Month.APRIL, 18);
        doctor = new Doctor(1, "Hirday", "Bajaj", "patient@fmail.com",
                "password", dob, null , null);
        doctor2 = new Doctor(2, "Debug", "Kid", "pc@fmail.com",
                "password", dob, null , null);
    }

    /*
     *  For now, just have a few test cases for each method. Testing should be a priority only once we are satisfied
     *  with our work.
     */

    // ----------------------------------- FOLLOW METHOD NAME CONVENTION -----------------------------------------------

    @Test
    void getAllDoctors_SUCCESS() {
        when(mockDoctorRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(doctor, doctor2))));

        Page<Doctor> doctorPage = doctorService.getAllDoctors(null);  // Get doctor page

        List<Doctor> doctors = doctorPage.getContent();  // Get list of doctors from doctor page

        // Test doctor list length
        assertEquals(2, doctors.size());

        // Test doctor 1 match
        assertEquals(doctor, doctors.get(0));

        // Test doctor 2 match
        assertEquals(doctor2, doctors.get(1));

    }

    @Test
    void getAllDoctors_FAIL() {
        // Test for fails
    }

    // ---------------------------- REMOVE THESE LINE COMMENTS AFTER READING -------------------------------------------

    @Test
    void getDoctorByID() {
        when(mockDoctorRepository.findByUserID(doctor.getUserID())).thenReturn(Optional.of(doctor));

        Doctor testDoctor = doctorService.getDoctorByID(doctor.getUserID());

        assertEquals(testDoctor, doctor);

    }

    //public Doctor(int userID, String firstName, String lastName, String email, String password,
    //                  LocalDate dateOfBirth, List<Role> roles, Specialty specialty

    @Test
    void updateDoctor() {

        LocalDate dob2 = LocalDate.of(2002, Month.JULY, 16);
        Doctor doctor2 = new Doctor(2, "Darrick", "Hong", "update@gmail.com", "update", dob2 , null, null);
        Doctor doctor3 = new Doctor(3, "Nim", "Tong", "update@gmail.com", "update", dob2, null,null);

        // For reference, BDDMockito has exactly the same functionality as Mockito. The difference lies in that BDDMockito provides methods that are in
        // more human-readable syntax. Pick whichever you want but be consistent. For more info: https://www.baeldung.com/bdd-mockito
        BDDMockito.given(mockDoctorRepository.findByUserID(doctor.getUserID())).willReturn(Optional.of(doctor));

        doctorService.updateDoctor(doctor.getUserID(), doctor2);

        assertEquals(1, doctor.getUserID());
        assertEquals("Darrick", doctor.getFirstName());
        assertEquals("Hong", doctor.getLastName());
        assertEquals("update@gmail.com", doctor.getEmail());
        assertEquals("update", doctor.getPassword());
        assertEquals(dob2,doctor.getDateOfBirth());
        assertNull(doctor.getRoles());
        assertNull(doctor.getSpecialty());

        doctorService.updateDoctor(doctor.getUserID(), doctor3);

        assertEquals(1, doctor.getUserID());
        assertEquals("Nim", doctor.getFirstName());
        assertEquals("Tong", doctor.getLastName());
        assertEquals("update@gmail.com", doctor.getEmail());
        assertEquals("update", doctor.getPassword());
        assertEquals(dob2,doctor.getDateOfBirth());
        assertNull(doctor.getRoles());
        assertNull(doctor.getSpecialty());
    }

    @Test
    void deleteDoctor() {
        when(mockDoctorRepository.findByUserID(doctor.getUserID())).thenReturn(Optional.of(doctor));

        doctorService.deleteDoctor(doctor.getUserID());

        verify(mockDoctorRepository).deleteById(doctor.getUserID());
    }
}