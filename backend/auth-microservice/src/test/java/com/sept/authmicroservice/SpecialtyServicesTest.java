package org.superfive.telemedicine;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.superfive.telemedicine.dto.SpecialtyDTO;
import org.superfive.telemedicine.exception.ResourceAlreadyExistsException;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.*;
import org.superfive.telemedicine.repository.SpecialtyRepository;
import org.superfive.telemedicine.service.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class SpecialtyServicesTest {
    private int specialtyID;
    private String specialtyName;
    private Set<Doctor> doctors;
    private SpecialtyRepository specialtyRepository;
    private SpecialtyService specialtyService;

    @BeforeEach
    public void setUp() {
        specialtyID = 1;
        specialtyName = "Neurology";
        Specialty specialty = new Specialty(specialtyID, specialtyName);
        specialtyRepository = Mockito.mock(SpecialtyRepository.class);
        specialtyService = new SpecialtyService(specialtyRepository);
    }

    @Test
    public void TestCreateSpecialty() {
        SpecialtyDTO specialtyDTO = new SpecialtyDTO(specialtyName);
        specialtyService.createSpecialty(specialtyDTO);

    }

    @Test
    public void TestUpdateSpecialty() {

    }
}
