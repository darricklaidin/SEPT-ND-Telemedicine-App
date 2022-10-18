package sept.superfive.authmicroservice.unit_test.controllers;

import sept.superfive.authmicroservice.controller.SpecialtyController;
import sept.superfive.authmicroservice.model.Specialty;
import sept.superfive.authmicroservice.payload.SpecialtyDTO;
import sept.superfive.authmicroservice.service.SpecialtyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

class SpecialtyControllerUnitTest {

    private SpecialtyController specialtyController;
    private SpecialtyService mockSpecialtyService;

    private Specialty specialty1;
    private Specialty specialty2;

    private final List<Specialty> specialties = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mockSpecialtyService = Mockito.mock(SpecialtyService.class);
        specialtyController = new SpecialtyController(mockSpecialtyService);

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
        when(mockSpecialtyService.getAllSpecialties(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(specialty1, specialty2))));

        Page<Specialty> specialtyPage = specialtyController.getAllSpecialties(null).getBody();

        if (specialtyPage == null) {
            fail("Specialty page is null");
        }

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
        when(mockSpecialtyService.getSpecialtyByID(specialty1.getSpecialtyID())).thenReturn(specialty1);

        // Test specialty1 matches
        assertEquals(specialtyController.getSpecialtyByID(specialty1.getSpecialtyID()).getBody(), specialty1);
    }

    @Test
    void createSpecialty() {
        SpecialtyDTO specialty1DTO = new SpecialtyDTO();
        specialty1DTO.setSpecialtyName("Cardiology");

        Specialty newSpecialty = new Specialty(specialty1DTO.getSpecialtyName());

        when(mockSpecialtyService.createSpecialty(specialty1DTO)).thenReturn(newSpecialty);

        Specialty createdSpecialty = specialtyController.createSpecialty(specialty1DTO).getBody();

        if (createdSpecialty == null) {
            fail("Created specialty is null");
        }

        when(mockSpecialtyService.getSpecialtyByID(createdSpecialty.getSpecialtyID()))
                .thenReturn(createdSpecialty);

        when(mockSpecialtyService.getAllSpecialties(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(specialty1, specialty2, createdSpecialty))));

        Page<Specialty> specialtyPage = specialtyController.getAllSpecialties(null).getBody();

        if (specialtyPage == null) {
            fail("Specialty page is null");
        }

        List<Specialty> retrievedSpecialties = specialtyPage.getContent();

        // Test specialty list length
        assertEquals(specialties.size() + 1, retrievedSpecialties.size());

        // Test get newSpecialty by ID
        assertEquals(createdSpecialty, specialtyController.getSpecialtyByID(createdSpecialty.getSpecialtyID()).getBody());

        // Test newSpecialty name matches
        assertEquals(newSpecialty.getSpecialtyName(), retrievedSpecialties.get(specialties.size()).getSpecialtyName());
    }

    @Test
    void updateSpecialty() {
        SpecialtyDTO specialtyDTO1 = new SpecialtyDTO();
        specialtyDTO1.setSpecialtyName("Childcare");

        Specialty newSpecialty = new Specialty(specialtyDTO1.getSpecialtyName());

        when(mockSpecialtyService.updateSpecialty(specialty1.getSpecialtyID(), specialtyDTO1)).thenAnswer(i -> {
            specialty1.setSpecialtyName(newSpecialty.getSpecialtyName());
            return specialty1;
        });

        when(mockSpecialtyService.getSpecialtyByID(specialty1.getSpecialtyID()))
                .thenReturn(newSpecialty);

        Specialty updatedSpecialty = specialtyController.updateSpecialty(specialtyDTO1, specialty1.getSpecialtyID()).getBody();

        when(mockSpecialtyService.getAllSpecialties(null))
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
        when(mockSpecialtyService.getSpecialtyByID(specialty1.getSpecialtyID())).thenReturn(specialty1);

        when(mockSpecialtyService.deleteSpecialty(specialty1.getSpecialtyID())).thenReturn(specialty1);

        Specialty deletedSpecialty = specialtyController.deleteSpecialty(specialty1.getSpecialtyID()).getBody();

        when(mockSpecialtyService.getAllSpecialties(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(specialty2))));

        Page<Specialty> specialtyPage = specialtyController.getAllSpecialties(null).getBody();

        if (specialtyPage == null) {
            fail("Specialty page is null");
        }

        List<Specialty> retrievedSpecialties = specialtyPage.getContent();

        // Test specialty list length
        assertEquals(specialties.size() - 1, retrievedSpecialties.size());

        // Test that specialty1 has been deleted
        assertEquals(specialty2, retrievedSpecialties.get(0));

        // Test that deleted specialty matches specialty1
        assertEquals(specialty1, deletedSpecialty);
    }
}