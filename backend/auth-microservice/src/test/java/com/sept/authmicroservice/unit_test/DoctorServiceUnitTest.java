package com.sept.authmicroservice.unit_test;

import com.sept.authmicroservice.repository.DoctorRepository;
import com.sept.authmicroservice.repository.UserRepository;
import com.sept.authmicroservice.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.BDDMockito;
import org.junit.jupiter.api.BeforeEach;
import com.sept.authmicroservice.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceUnitTest {

    private DoctorRepository mockDoctorRepository;
    private UserRepository mockUserRepository;
    private DoctorService doctorService;

    private Doctor doctor;
    private Doctor doctor2;
    private Doctor doctor3;
    private Doctor doctor4;

    LocalDate dob;
    LocalDate dob2;
    LocalDate dob3;
    LocalDate dob4;


    @BeforeEach
    void setUp() {
        mockDoctorRepository = Mockito.mock(DoctorRepository.class);
        mockUserRepository = Mockito.mock(UserRepository.class);

        doctorService = new DoctorService(mockDoctorRepository, mockUserRepository);

        dob = LocalDate.of(2000, Month.JANUARY, 1);
        dob2 = LocalDate.of(2001, Month.FEBRUARY, 2);
        dob3 = LocalDate.of(2002, Month.MARCH, 3);
        dob4 = LocalDate.of(2003, Month.APRIL, 4);

        doctor = new Doctor(1, "Hirday", "Bajaj", "patient@fmail.com", "password", dob, null , null);
        doctor2 = new Doctor(2, "Mohamed", "Mahamed", "pc@fmail.coa", "password", dob2, null , null);
        doctor3 = new Doctor(3, "Bryan", "Hong", "update@gmail.com", "update", dob3 , null, null);
        doctor4 = new Doctor(4, "Nim", "W", "update@gmail.com", "update", dob4, null,null);

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

//    @Test
//    void getAllDoctors_FAIL() {
//
//    }

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

        // For reference, BDDMockito has exactly the same functionality as Mockito. The difference lies in that BDDMockito provides methods that are in
        // more human-readable syntax. Pick whichever you want but be consistent. For more info: https://www.baeldung.com/bdd-mockito
        BDDMockito.given(mockDoctorRepository.findByUserID(doctor.getUserID())).willReturn(Optional.of(doctor)); //doctor = USERID 1

        Doctor updateDoctor = new Doctor(-1, "Darrick", "Laidin", "update@gmail.com", "update", dob3 , null, null);

        doctorService.updateDoctor(doctor.getUserID(), updateDoctor); //doctor = USERID_1

        assertEquals(1, doctor.getUserID());
        assertEquals("Darrick", doctor.getFirstName());
        assertEquals("Laidin", doctor.getLastName());
        assertEquals("update@gmail.com", doctor.getEmail());
//        assertEquals("update", doctor.getPassword());
        assertEquals(dob3,doctor.getDateOfBirth());
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