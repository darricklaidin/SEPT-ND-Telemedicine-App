package org.superfive.telemedicine.unit_test.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.superfive.telemedicine.controller.AvailabilityController;
import org.superfive.telemedicine.dto.AvailabilityDTO;
import org.superfive.telemedicine.exception.InvalidTimeException;
import org.superfive.telemedicine.exception.ResourceAlreadyExistsException;
import org.superfive.telemedicine.model.Availability;
import org.superfive.telemedicine.service.AvailabilityService;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class
AvailabilityControllerUnitTest {

    private AvailabilityService mockAvailabilityService;
    private AvailabilityController availabilityController;

    private Availability availability1;
    private Availability availability2;

    private final List<Availability> availabilities = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mockAvailabilityService = Mockito.mock(AvailabilityService.class);
        availabilityController = new AvailabilityController(mockAvailabilityService);

        availability1 = new Availability(1, DayOfWeek.MONDAY,
                LocalTime.of(9, 0, 0), LocalTime.of(17, 0, 0),1);
        availability2 = new Availability(2, DayOfWeek.FRIDAY,
                LocalTime.of(12, 0, 0), LocalTime.of(22, 0, 0),1);

        availabilities.add(availability1);
        availabilities.add(availability2);
    }

    @Test
    void getAllAvailabilities() {
        when(mockAvailabilityService.getAllAvailabilities(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(availability1, availability2))));

        Page<Availability> availabilityPage = availabilityController.getAllAvailabilities(null).getBody();  // Get doctor page

        if (availabilityPage == null) {
            fail("Availability page is null");
        }

        List<Availability> retrievedAvailabilities = availabilityPage.getContent();

        assertEquals(availabilities.size(), retrievedAvailabilities.size());

        for (int i = 0; i < availabilities.size(); i++) {
            assertEquals(availabilities.get(i), retrievedAvailabilities.get(i));
        }
    }

    @Test
    void getAvailabilityByID() {
        when(mockAvailabilityService.getAvailabilityByID(availability1.getAvailabilityID())).thenReturn(availability1);
        Availability testAvailability = availabilityController.getAvailabilityByID(availability1.getAvailabilityID()).getBody();
        assertEquals(testAvailability, availability1);
    }

    @Test
    void getDoctorAvailabilities() {
        when(mockAvailabilityService.getDoctorAvailabilities(availability1.getDoctorID(),null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(availability1))));

        Page<Availability> availabilitiesDoctorPage = availabilityController.getDoctorAvailabilities(availability1.getDoctorID(),null)
                .getBody();  // Get doctor page

        if (availabilitiesDoctorPage == null) {
            fail("Availability page is null");
        }

        List<Availability> doctorAvailability = availabilitiesDoctorPage.getContent();

        assertEquals(1, doctorAvailability.size());

        assertEquals(availability1, doctorAvailability.get(0));
    }

    @Test
    void addAvailability() {
        AvailabilityDTO invalidTimeAvailabilityDTO = new AvailabilityDTO(3,
                2, LocalTime.of(11, 0, 0), LocalTime.of(10, 0, 0),
                1);

        when(mockAvailabilityService.addAvailability(invalidTimeAvailabilityDTO))
                .thenThrow(InvalidTimeException.class);

        assertThrows(InvalidTimeException.class, () -> availabilityController.addAvailability(invalidTimeAvailabilityDTO));

        AvailabilityDTO availability3DTO = new AvailabilityDTO(3,
                2, LocalTime.of(10, 0, 0), LocalTime.of(11, 0, 0),
                1);

        when(mockAvailabilityService.addAvailability(availability3DTO)).thenThrow(ResourceAlreadyExistsException.class);

        assertThrows(ResourceAlreadyExistsException.class, () -> availabilityController.addAvailability(availability3DTO));

        AvailabilityDTO availability4DTO = new AvailabilityDTO(4, 2,
                LocalTime.of(10, 0, 0), LocalTime.of(11, 0, 0), 1);

        when(mockAvailabilityService.addAvailability(availability4DTO))
                .thenReturn(new Availability(availability4DTO.getAvailabilityID(),
                DayOfWeek.of(availability4DTO.getDayOfWeek()), availability4DTO.getStartTime(), availability4DTO.getEndTime(),
                availability4DTO.getDoctorID()));

        Availability availability4 = availabilityController.addAvailability(availability4DTO).getBody();

        if (availability4 == null) {
            fail("Availability 4 is null");
        }

        assertEquals(availability4DTO.getAvailabilityID(), availability4.getAvailabilityID());
        assertEquals(availability4DTO.getStartTime(), availability4.getStartTime());
        assertEquals(availability4DTO.getEndTime(), availability4.getEndTime());
        assertEquals(availability4DTO.getDayOfWeek(), availability4.getDayOfWeek().getValue());
        assertEquals(availability4DTO.getDoctorID(), availability4.getDoctorID());
    }

    @Test
    void rescheduleAvailability() {
        when(mockAvailabilityService.getAvailabilityByID(availability1.getAvailabilityID())).thenReturn(availability1);

        AvailabilityDTO invalidTimeAvailabilityDTO = new AvailabilityDTO(3,
                2, LocalTime.of(11, 0, 0), LocalTime.of(10, 0, 0),
                1);

        when(mockAvailabilityService.rescheduleAvailability(invalidTimeAvailabilityDTO, availability1.getAvailabilityID()))
                .thenThrow(InvalidTimeException.class);

        assertThrows(InvalidTimeException.class,
                () -> availabilityController.rescheduleAvailability(invalidTimeAvailabilityDTO, 1));

        AvailabilityDTO availabilityDTO = new AvailabilityDTO(1, 4,
                LocalTime.of(8, 0, 0), LocalTime.of(20, 0, 0), 2);

        Availability oldAvailability1 = new Availability(availability1.getAvailabilityID(), availability1.getDayOfWeek(),
                availability1.getStartTime(), availability1.getEndTime(), availability1.getDoctorID());

        when(mockAvailabilityService.rescheduleAvailability(availabilityDTO, availability1.getAvailabilityID())).thenAnswer(i -> {
            availability1.setStartTime(availabilityDTO.getStartTime());
            availability1.setEndTime(availabilityDTO.getEndTime());
            availability1.setDayOfWeek(DayOfWeek.of(availabilityDTO.getDayOfWeek()));
            availability1.setDoctorID(availabilityDTO.getDoctorID());
            return availability1;
        });

        availabilityController.rescheduleAvailability(availabilityDTO, availability1.getAvailabilityID());

        assertEquals(availabilityDTO.getAvailabilityID(), availability1.getAvailabilityID());
        assertEquals(DayOfWeek.of(availabilityDTO.getDayOfWeek()), availability1.getDayOfWeek());
        assertEquals(availabilityDTO.getStartTime(), availability1.getStartTime());
        assertEquals(availabilityDTO.getEndTime(), availability1.getEndTime());
        assertEquals(availabilityDTO.getDoctorID(), availability1.getDoctorID());

        assertNotEquals(oldAvailability1.getDayOfWeek(), availability1.getDayOfWeek());
        assertNotEquals(oldAvailability1.getStartTime(), availability1.getStartTime());
        assertNotEquals(oldAvailability1.getEndTime(), availability1.getEndTime());
        assertNotEquals(oldAvailability1.getDoctorID(), availability1.getDoctorID());
    }

    @Test
    void deleteAvailability() {
        when(mockAvailabilityService.getAvailabilityByID(availability1.getAvailabilityID())).thenReturn(availability1);

        when(mockAvailabilityService.deleteAvailability(availability1.getAvailabilityID())).thenReturn(availability1);

        Availability deletedAvailability = availabilityController.deleteAvailability(availability1.getAvailabilityID()).getBody();

        when(mockAvailabilityService.getAllAvailabilities(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(availability2))));

        Page<Availability> availabilityPage = availabilityController.getAllAvailabilities(null).getBody();

        if (availabilityPage == null) {
            fail("Availability page is null");
        }

        List<Availability> retrievedAvailabilities = availabilityPage.getContent();

        // Test availability list length
        assertEquals(availabilities.size() - 1, retrievedAvailabilities.size());

        // Test that availability1 has been deleted
        assertEquals(availability2, retrievedAvailabilities.get(0));

        // Test that deleted availability matches availability1
        assertEquals(availability1, deletedAvailability);
    }
}