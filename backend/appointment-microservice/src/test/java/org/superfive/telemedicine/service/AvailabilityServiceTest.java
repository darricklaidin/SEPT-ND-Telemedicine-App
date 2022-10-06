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
    }

    @Test
    void getAvailabilityByID() {
    }

    @Test
    void getDoctorAvailabilities() {
    }

    @Test
    void addAvailability() {
    }

    @Test
    void rescheduleAvailability() {
    }

    @Test
    void deleteAvailability() {
    }
}