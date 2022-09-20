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
    }

    @Test
    void getAllSpecialties() {

    }

    @Test
    void getSpecialtyByID() {
    }

    @Test
    void getSpecialtyByName() {
    }

    @Test
    void createSpecialty() {
        specialtyDTO = new SpecialtyDTO();
        specialtyDTO.setSpecialtyName(specialtyName);
        Specialty newSpecialty = specialtyService.createSpecialty(specialtyDTO);
        Assertions.assertTrue(newSpecialty.equals(specialty));
    }

    @Test
    void updateSpecialty() {
    }

    @Test
    void deleteSpecialty() {
    }
}