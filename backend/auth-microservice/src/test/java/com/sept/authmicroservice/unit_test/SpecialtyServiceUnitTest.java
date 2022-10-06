package com.sept.authmicroservice.unit_test;

import com.sept.authmicroservice.repository.DoctorRepository;
import com.sept.authmicroservice.repository.UserRepository;
import com.sept.authmicroservice.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.BDDMockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sept.authmicroservice.exception.ResourceAlreadyExistsException;
import com.sept.authmicroservice.model.*;
import com.sept.authmicroservice.repository.SpecialtyRepository;
import com.sept.authmicroservice.service.SpecialtyService;
import com.sept.authmicroservice.payload.SpecialtyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpecialtyServiceUnitTest {

    private int specialtyID;
    private String specialtyName;
    private Set<Doctor> doctors;
    private SpecialtyService specialtyService;
    private SpecialtyDTO specialtyDTO;
    private Specialty specialty;
    private Specialty specialty2;

    private SpecialtyRepository mockSpecialtyRepository;

    @BeforeEach
    void setUp() {

        mockSpecialtyRepository = Mockito.mock(SpecialtyRepository.class);

        specialtyService = new SpecialtyService(mockSpecialtyRepository);

        specialty = new Specialty("Surgery");
        specialty2 = new Specialty("Optometry");
    }

    @Test
    void getAllSpecialties() {

        when(mockSpecialtyRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(specialty, specialty2))));

        Page<Specialty> specialtyPage = specialtyService.getAllSpecialties(null);  // Get doctor page

        List<Specialty> specialties = specialtyPage.getContent();  // Get list of doctors from doctor page

        // Test specialty list length
        assertEquals(2, specialties.size());

        // Test specialty match
        assertEquals(specialty, specialties.get(0));

        // Test specialty2 match
        assertEquals(specialty2, specialties.get(1));
    }

    @Test
    void getSpecialtyByID() {

        when(mockSpecialtyRepository.findBySpecialtyID(specialtyID)).thenReturn(Optional.of(specialty));
        Assertions.assertTrue(specialtyService.getSpecialtyByID(specialtyID).equals(specialty));
    }

    @Test
    void getSpecialtyByName() {
        when(mockSpecialtyRepository.findBySpecialtyName(specialtyName)).thenReturn(Optional.of(specialty));
        Assertions.assertTrue(specialtyService.getSpecialtyByName(specialtyName).equals(specialty));
    }

    @Test
    void createSpecialty() {

        BDDMockito.given(mockSpecialtyRepository.findBySpecialtyID(specialty.getSpecialtyID())).willReturn(Optional.of(specialty));

        SpecialtyDTO specialtyDTO1 = new SpecialtyDTO();

        specialtyDTO1.setSpecialtyName("Cardiology");

        System.out.println(specialtyDTO1.getSpecialtyName());

        Specialty tester = specialtyService.createSpecialty(specialtyDTO1);

        verify(mockSpecialtyRepository).save(tester);

    }


    @Test
    void updateSpecialty() {

        SpecialtyDTO specialtyDTO1 = new SpecialtyDTO();

        specialtyDTO1.setSpecialtyName("Child");

        BDDMockito.given(mockSpecialtyRepository.findBySpecialtyID(specialty.getSpecialtyID())).willReturn(Optional.of(specialty));

        specialtyService.updateSpecialty(specialty.getSpecialtyID(), specialtyDTO1);

        assertEquals("Child", specialty.getSpecialtyName());

    }

    @Test
    void deleteSpecialty() {
        when(mockSpecialtyRepository.findBySpecialtyID(specialty.getSpecialtyID())).thenReturn(Optional.of(specialty));

        specialtyService.deleteSpecialty(specialty.getSpecialtyID());

        verify(mockSpecialtyRepository).deleteById(specialty.getSpecialtyID());
    }
}