package com.sept.authmicroservice.unit_test;

import com.sept.authmicroservice.model.Doctor;
import com.sept.authmicroservice.model.Specialty;
import com.sept.authmicroservice.repository.DoctorRepository;
import com.sept.authmicroservice.repository.UserRepository;
import com.sept.authmicroservice.service.DoctorService;
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

class DoctorServiceUnitTest {

    private DoctorRepository mockDoctorRepository;
    private DoctorService doctorService;

    private Doctor doctor1;
    private Doctor doctor2;
    private final List<Doctor> doctors = new ArrayList<>();

    private Specialty specialty2;


    LocalDate dob1;
    LocalDate dob2;


    @BeforeEach
    void setUp() {
        mockDoctorRepository = Mockito.mock(DoctorRepository.class);
        UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
        doctorService = new DoctorService(mockDoctorRepository, mockUserRepository);

        dob1 = LocalDate.of(2000, Month.JANUARY, 1);
        dob2 = LocalDate.of(2001, Month.FEBRUARY, 2);

        Specialty specialty1 = new Specialty("Cardiology");
        specialty2 = new Specialty("Surgery");

        doctor1 = new Doctor(1, "Hirday", "Bajaj", "patient@fmail.com", "password", dob1, null , specialty1);
        doctor2 = new Doctor(2, "Mohamed", "Mahamed", "pc@fmail.coa", "password", dob2, null , specialty2);

        doctors.add(doctor1);
        doctors.add(doctor2);
    }

    @Test
    void getAllDoctors() {
        when(mockDoctorRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(doctor1, doctor2))));

        Page<Doctor> doctorPage = doctorService.getAllDoctors(null);

        List<Doctor> retrievedDoctors = doctorPage.getContent();

        // Test doctor list length
        assertEquals(2, doctors.size());

        // Test doctor 1 match
        assertEquals(doctor1, doctors.get(0));

        // Test doctor 2 match
        assertEquals(doctor2, doctors.get(1));

        // Test each doctor matches
        for (int i = 0; i < doctors.size(); i++) {
            assertEquals(doctors.get(i), retrievedDoctors.get(i));
        }
    }

    @Test
    void getDoctorByID() {
        when(mockDoctorRepository.findByUserID(doctor1.getUserID())).thenReturn(Optional.of(doctor1));
        assertEquals(doctor1, doctorService.getDoctorByID(doctor1.getUserID()));
    }

    @Test
    void updateDoctor() {
        Doctor updateDoctor = new Doctor(doctor1.getUserID(), "Darrick", "Laidin", "updatedl@gmail.com",
                "update", dob2 , null, specialty2);
        updateDoctor.setAccountStatus(false);

        Doctor oldDoctor1 = new Doctor(doctor1.getUserID(), doctor1.getFirstName(), doctor1.getLastName(),
                doctor1.getEmail(), doctor1.getPassword(), doctor1.getDateOfBirth(), null, doctor1.getSpecialty());

        when(mockDoctorRepository.findByUserID(doctor1.getUserID())).thenReturn(Optional.of(doctor1));
        when(mockDoctorRepository.save(doctor1)).thenAnswer(i -> {
            doctor1.setAccountStatus(updateDoctor.isEnabled());
            doctor1.setFirstName(updateDoctor.getFirstName());
            doctor1.setLastName(updateDoctor.getLastName());
            doctor1.setEmail(updateDoctor.getEmail());
            doctor1.setPassword(updateDoctor.getPassword());
            doctor1.setDateOfBirth(updateDoctor.getDateOfBirth());
            doctor1.setSpecialty(updateDoctor.getSpecialty());
            return doctor1;
        });

        doctorService.updateDoctor(doctor1.getUserID(), updateDoctor);

        assertEquals(updateDoctor.getFirstName(), doctor1.getFirstName());
        assertEquals(updateDoctor.getLastName(), doctor1.getLastName());
        assertEquals(updateDoctor.getEmail(), doctor1.getEmail());
        assertEquals(updateDoctor.getPassword(), doctor1.getPassword());
        assertEquals(updateDoctor.getDateOfBirth(), doctor1.getDateOfBirth());
        assertEquals(updateDoctor.isEnabled(), doctor1.isEnabled());
        assertEquals(updateDoctor.getSpecialty(), doctor1.getSpecialty());

        assertNotEquals(oldDoctor1.getFirstName(), doctor1.getFirstName());
        assertNotEquals(oldDoctor1.getLastName(), doctor1.getLastName());
        assertNotEquals(oldDoctor1.getEmail(), doctor1.getEmail());
        assertNotEquals(oldDoctor1.getPassword(), doctor1.getPassword());
        assertNotEquals(oldDoctor1.getDateOfBirth(), doctor1.getDateOfBirth());
        assertNotEquals(oldDoctor1.isEnabled(), doctor1.isEnabled());
        assertNotEquals(oldDoctor1.getSpecialty(), doctor1.getSpecialty());

        assertNull(doctor1.getRoles());
    }

    @Test
    void deleteDoctor() {
        when(mockDoctorRepository.findByUserID(doctor1.getUserID())).thenReturn(Optional.of(doctor1));

        doNothing().when(mockDoctorRepository).deleteById(doctor1.getUserID());

        Doctor deletedDoctor = doctorService.deleteDoctor(doctor1.getUserID());

        when(mockDoctorRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(doctor2))));

        Page<Doctor> doctorPage = doctorService.getAllDoctors(null);
        List<Doctor> retrievedDoctors = doctorPage.getContent();

        // Test doctor list length
        assertEquals(doctors.size() - 1, retrievedDoctors.size());

        // Test that doctor1 has been deleted
        assertEquals(doctor2, retrievedDoctors.get(0));

        // Test that deleted doctor matches doctor1
        assertEquals(doctor1, deletedDoctor);
    }
}