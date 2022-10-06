package org.superfive.telemedicine.service;

import com.sept.authmicroservice.model.Doctor;
import com.sept.authmicroservice.model.Specialty;
import com.sept.authmicroservice.payload.SpecialtyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.superfive.telemedicine.dto.AvailabilityDTO;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Availability;
import org.superfive.telemedicine.repository.AppointmentRepository;
import org.superfive.telemedicine.repository.AvailabilityRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class
AvailabilityServiceTest {

    private int availabilityID;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek dayOfWeek2;
    private LocalTime startTime2;
    private LocalTime endTime2;
    private int doctorID;
    private AvailabilityRepository mockAvailabilityRepository;
    private AvailabilityService availabilityService;

    private Availability availability;

    @BeforeEach
    void setUp() {
        mockAvailabilityRepository = Mockito.mock(AvailabilityRepository.class);
        availabilityService = new AvailabilityService(mockAvailabilityRepository);

        dayOfWeek = DayOfWeek.MONDAY;
        startTime = LocalTime.of(8,0,0);
        endTime = LocalTime.of(17,0,0);

        availability = new Availability(1,dayOfWeek,startTime,endTime,1);

    }


    @Test
    void getAllAvailabilities() {
        when(mockAvailabilityRepository.findAllBy(null)).thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(availability))));

        Page<Availability> availabilityPage = availabilityService.getAllAvailabilities(null);  // Get doctor page

        List<Availability> availabilities = availabilityPage.getContent();
    }

    @Test
    void getAvailabilityByID() {
        when(mockAvailabilityRepository.findByAvailabilityID(availability.getAvailabilityID())).thenReturn(Optional.of(availability));

        Availability testAvailability = availabilityService.getAvailabilityByID(availability.getAvailabilityID());

        assertEquals(testAvailability, availability);

    }

    @Test
    void getDoctorAvailabilities() {
        when(mockAvailabilityRepository.findByDoctorID(availability.getDoctorID(),null)).thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(availability))));

        Page<Availability> availabilitiesDoctorPage = availabilityService.getDoctorAvailabilities(availability.getDoctorID(),null);  // Get doctor page

        List<Availability> doctorAvailability = availabilitiesDoctorPage.getContent();

        assertEquals(1, doctorAvailability.size());

        assertEquals(availability, doctorAvailability.get(0));
    }

    @Test
    void addAvailability() {
        //TODO : Need to complete this section -- TEST WHERE FAIL CONDITION WHERE AVAILABILITY ALREADY EXISTS
        BDDMockito.given(mockAvailabilityRepository.findByAvailabilityID(availability.getAvailabilityID())).willReturn(Optional.of(availability));

        dayOfWeek2 = DayOfWeek.MONDAY;
        startTime2 = LocalTime.of(12,0,0);
        endTime2 = LocalTime.of(14,0,0);

        Availability availability2 = new Availability(2,dayOfWeek2,startTime2,endTime2,1);

        AvailabilityDTO availabilityDTO1 = new AvailabilityDTO(availability2.getAvailabilityID(), 7, availability2.getStartTime(), availability2.getEndTime(), availability2.getDoctorID());

        Availability tester = availabilityService.addAvailability(availabilityDTO1);

        verify(mockAvailabilityRepository).save(tester);

    }

    @Test
    void rescheduleAvailability() {
        BDDMockito.given(mockAvailabilityRepository.findByAvailabilityID(availability.getAvailabilityID())).willReturn(Optional.of(availability)); //doctor = USERID 1

        DayOfWeek dayOfWeek_update = DayOfWeek.THURSDAY;
        LocalTime startTime_update = LocalTime.of(10,0,0);
        LocalTime endTime_update = LocalTime.of(14,0,0);

        Availability updateAvailability = new Availability(1,dayOfWeek_update,startTime_update,endTime_update,1);

        AvailabilityDTO availabilityDTO1 = new AvailabilityDTO(updateAvailability.getAvailabilityID(), 7, updateAvailability.getStartTime(), updateAvailability.getEndTime(), updateAvailability.getDoctorID());



        availabilityService.rescheduleAvailability(availabilityDTO1, updateAvailability.getAvailabilityID());

        assertEquals(1, availability.getAvailabilityID());
        assertEquals(DayOfWeek.SUNDAY, availability.getDayOfWeek());
        assertEquals(LocalTime.of(10,0,0), availability.getStartTime());
        assertEquals(LocalTime.of(14,0,0), availability.getEndTime());
        assertEquals(1, availability.getDoctorID());

    }

    @Test
    void deleteAvailability() {
        when(mockAvailabilityRepository.findByAvailabilityID(availability.getAvailabilityID())).thenReturn(Optional.of(availability));

        availabilityService.deleteAvailability(availability.getAvailabilityID());

        verify(mockAvailabilityRepository).deleteById(availability.getAvailabilityID());
    }
}