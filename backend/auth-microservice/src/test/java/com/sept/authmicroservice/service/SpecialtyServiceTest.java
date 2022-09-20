package com.sept.authmicroservice.service;

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
import com.sept.authmicroservice.repository.SpecialtyRepository;
import com.sept.authmicroservice.service.SpecialtyService;
import com.sept.authmicroservice.payload.SpecialtyDTO;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
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

class SpecialtyServiceTest {

    private int specialtyID;
    private String specialtyName;
    private Set<Doctor> doctors;
    private SpecialtyRepository specialtyRepository;
    private SpecialtyService specialtyService;
    private SpecialtyDTO specialtyDTO;
    private Specialty specialty;

    @BeforeEach
    void setUp() {
        specialtyID = 1;
        specialtyName = "Neurology";
        specialty = new Specialty(specialtyName);
        specialtyRepository = Mockito.mock(SpecialtyRepository.class);
        specialtyService = new SpecialtyService(specialtyRepository);
        specialtyDTO = new SpecialtyDTO();
        specialtyDTO.setSpecialtyName(specialtyName);
        specialty.setSpecialtyID(1);
    }

//    @Test
//    void getAllSpecialties() {
//        Pageable pageable = null;
//        Page<Specialty> specialties = null;
//        when(specialtyRepository.findAllBy(pageable)).thenReturn(specialties);
//    }

    @Test
    void getSpecialtyByID() {

        when(specialtyRepository.findBySpecialtyID(specialtyID)).thenReturn(Optional.of(specialty));
        Assertions.assertTrue(specialtyService.getSpecialtyByID(specialtyID).equals(specialty));
    }

    @Test
    void getSpecialtyByName() {
        when(specialtyRepository.findBySpecialtyName(specialtyName)).thenReturn(Optional.of(specialty));
        Assertions.assertTrue(specialtyService.getSpecialtyByName(specialtyName).equals(specialty));
    }

    @Test
    void createSpecialty() {
//        when(specialtyRepository.save(specialty)).thenReturn(specialty);
        Specialty newSpecialty = specialtyService.createSpecialty(specialtyDTO);
        Assertions.assertTrue(newSpecialty.getSpecialtyName().equals(specialty.getSpecialtyName()));
//        verify(specialtyRepository).save(specialty);
    }

    @Test
    public void TestCreate2Patients_FALSE() throws ResourceAlreadyExistsException{
        specialtyService.createSpecialty(specialtyDTO);
        when(specialtyRepository.findBySpecialtyName(specialtyDTO.getSpecialtyName())).thenReturn(Optional.of(specialty));
        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> specialtyService.createSpecialty(specialtyDTO));
    }

    @Test
    void updateSpecialty() {

        Specialty specialty1 = new Specialty("Surgery");
        Specialty specialty2 = new Specialty("Optometry");

        SpecialtyDTO specialtyDTO2 = new SpecialtyDTO();
        specialtyDTO2.setSpecialtyName("Optometry");


        BDDMockito.given(specialtyRepository.findBySpecialtyID(specialty.getSpecialtyID())).willReturn(Optional.of(specialty));

        specialtyService.updateSpecialty(specialty.getSpecialtyID(), specialtyDTO2);

        assertEquals(1, specialty.getSpecialtyID());
        assertEquals("Optometry", specialty.getSpecialtyName());
    }

    @Test
    void deleteSpecialty() {
        when(specialtyRepository.findBySpecialtyID(specialty.getSpecialtyID())).thenReturn(Optional.of(specialty));

        specialtyService.deleteSpecialty(specialty.getSpecialtyID());

        verify(specialtyRepository).deleteById(specialty.getSpecialtyID());
    }
}