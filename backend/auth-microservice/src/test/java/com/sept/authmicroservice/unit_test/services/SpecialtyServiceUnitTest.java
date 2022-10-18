package com.sept.authmicroservice.unit_test.services;

import com.sept.authmicroservice.model.Specialty;
import com.sept.authmicroservice.payload.SpecialtyDTO;
import com.sept.authmicroservice.repository.SpecialtyRepository;
import com.sept.authmicroservice.service.SpecialtyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class SpecialtyServiceUnitTest {

    private SpecialtyService specialtyService;
    private SpecialtyRepository mockSpecialtyRepository;

    private Specialty specialty1;
    private Specialty specialty2;

    private final List<Specialty> specialties = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mockSpecialtyRepository = Mockito.mock(SpecialtyRepository.class);
        specialtyService = new SpecialtyService(mockSpecialtyRepository);

        // Create new specialties
        specialty1 = new Specialty("Surgery");
        specialty1.setSpecialtyID(1);
        specialty2 = new Specialty("Optometry");
        specialty2.setSpecialtyID(2);

        // Add specialties to specialties list
        specialties.add(specialty1);
        specialties.add(specialty2);
    }

    @Test
    void getAllSpecialties() {
        when(mockSpecialtyRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(specialty1, specialty2))));

        Page<Specialty> specialtyPage = specialtyService.getAllSpecialties(null);
        List<Specialty> retrievedSpecialties = specialtyPage.getContent();

        // Test specialty list length
        assertEquals(specialties.size(), retrievedSpecialties.size());

        // Test each specialty matches
        for (int i = 0; i < specialties.size(); i++) {
            assertEquals(specialties.get(i), retrievedSpecialties.get(i));
        }
    }

    @Test
    void getSpecialtyByID() {
        when(mockSpecialtyRepository.findBySpecialtyID(specialty1.getSpecialtyID())).thenReturn(Optional.of(specialty1));

        // Test specialty1 matches
        assertEquals(specialtyService.getSpecialtyByID(specialty1.getSpecialtyID()), specialty1);
    }

    @Test
    void getSpecialtyByName() {
        when(mockSpecialtyRepository.findBySpecialtyName(specialty1.getSpecialtyName())).thenReturn(Optional.of(specialty1));

        // Test specialty1 matches
        assertEquals(specialtyService.getSpecialtyByName(specialty1.getSpecialtyName()), specialty1);
    }

    @Test
    void createSpecialty() {
        SpecialtyDTO specialty1DTO = new SpecialtyDTO();
        specialty1DTO.setSpecialtyName("Cardiology");

        Specialty newSpecialty = new Specialty(specialty1DTO.getSpecialtyName());

        when(mockSpecialtyRepository.save(newSpecialty)).thenReturn(newSpecialty);

        Specialty createdSpecialty = specialtyService.createSpecialty(specialty1DTO);

        when(mockSpecialtyRepository.findBySpecialtyID(createdSpecialty.getSpecialtyID()))
                .thenReturn(Optional.of(createdSpecialty));

        when(mockSpecialtyRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(specialty1, specialty2, createdSpecialty))));

        Page<Specialty> specialtyPage = specialtyService.getAllSpecialties(null);
        List<Specialty> retrievedSpecialties = specialtyPage.getContent();

        // Test specialty list length
        assertEquals(specialties.size() + 1, retrievedSpecialties.size());

        // Test get newSpecialty by ID
        assertEquals(createdSpecialty, specialtyService.getSpecialtyByID(createdSpecialty.getSpecialtyID()));

        // Test newSpecialty name matches
        assertEquals(newSpecialty.getSpecialtyName(), retrievedSpecialties.get(specialties.size()).getSpecialtyName());
    }

    @Test
    void updateSpecialty() {
        SpecialtyDTO specialtyDTO1 = new SpecialtyDTO();
        specialtyDTO1.setSpecialtyName("Childcare");

        Specialty newSpecialty = new Specialty(specialtyDTO1.getSpecialtyName());

        when(mockSpecialtyRepository.save(newSpecialty)).thenAnswer(i -> {
            specialty1.setSpecialtyName(newSpecialty.getSpecialtyName());
            return specialty1;
        });

        when(mockSpecialtyRepository.findBySpecialtyID(specialty1.getSpecialtyID()))
                .thenReturn(Optional.of(newSpecialty));

        Specialty updatedSpecialty = specialtyService.updateSpecialty(specialty1.getSpecialtyID(), specialtyDTO1);

        when(mockSpecialtyRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(specialty1, specialty2))));

        // Test that specialty1's name has been updated
        assertEquals("Childcare", specialty1.getSpecialtyName());

        // Test that the id of specialty 1 remains unchanged
        assertEquals(1, specialty1.getSpecialtyID());

        // Test that updatedSpecialty matches with specialty1
        assertEquals(specialty1, updatedSpecialty);
    }

    @Test
    void deleteSpecialty() {
        when(mockSpecialtyRepository.findBySpecialtyID(specialty1.getSpecialtyID())).thenReturn(Optional.of(specialty1));

        doNothing().when(mockSpecialtyRepository).deleteById(specialty1.getSpecialtyID());

        Specialty deletedSpecialty = specialtyService.deleteSpecialty(specialty1.getSpecialtyID());

        when(mockSpecialtyRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(specialty2))));

        Page<Specialty> specialtyPage = specialtyService.getAllSpecialties(null);
        List<Specialty> retrievedSpecialties = specialtyPage.getContent();

        // Test specialty list length
        assertEquals(specialties.size() - 1, retrievedSpecialties.size());

        // Test that specialty1 has been deleted
        assertEquals(specialty2, retrievedSpecialties.get(0));

        // Test that deleted specialty matches specialty1
        assertEquals(specialty1, deletedSpecialty);
    }
}