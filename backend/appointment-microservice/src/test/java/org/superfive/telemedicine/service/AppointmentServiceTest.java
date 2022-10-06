package org.superfive.telemedicine.service;

import com.sept.authmicroservice.model.Specialty;
import com.sept.authmicroservice.payload.SpecialtyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.repository.AppointmentRepository;
import org.superfive.telemedicine.repository.AvailabilityRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AppointmentServiceTest {

    private int appointmentID;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date2;
    private LocalTime startTime2;
    private LocalTime endTime2;

    private String appointmentStatus;
    private int doctorID;
    private int patientID;

    private AppointmentService appointmentService;
    private AppointmentRepository mockAppointmentRepository;
    private AvailabilityRepository mockAvailabilityRepository;

    private Appointment appointment;
    private Appointment appointment2;
    private Appointment appointment3;

    @BeforeEach
    void setUp() {
        mockAppointmentRepository = Mockito.mock(AppointmentRepository.class);
        appointmentService = new AppointmentService(mockAppointmentRepository,mockAvailabilityRepository);

        date = LocalDate.of(2000, Month.JANUARY,1);
        startTime = LocalTime.of(10,0,0);
        endTime = LocalTime.of(10,30,0);

        date2 = LocalDate.of(2000, Month.JANUARY,1);
        startTime2 = LocalTime.of(11,0,0);
        endTime2 = LocalTime.of(11,30,0);

        appointment = new Appointment(1, date, startTime, endTime, "UPCOMING", 1,1);
        appointment2 = new Appointment(2, date2, startTime2, endTime2, "UPCOMING", 1,2);
        appointment3 = new Appointment(3, date, startTime, endTime, "UPCOMING", 2,2);

    }

    @Test
    void getAllAppointments() {
        when(mockAppointmentRepository.findAllBy(null)).thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(appointment, appointment2))));

        Page<Appointment> appointmentPage = appointmentService.getAllAppointments(null);  // Get doctor page

        List<Appointment> appointments = appointmentPage.getContent();  // Get list of doctors from doctor page

        // Test doctor list length
        assertEquals(2, appointments.size());

        // Test doctor 1 match
        assertEquals(appointment, appointments.get(0));

        // Test doctor 2 match
        assertEquals(appointment2, appointments.get(1));

    }

    @Test
    void getAppointmentByID() {
        when(mockAppointmentRepository.findByAppointmentID(appointment.getAppointmentID())).thenReturn(Optional.of(appointment));

        Appointment testAppointment = appointmentService.getAppointmentByID(appointment.getAppointmentID());

        assertEquals(testAppointment, appointment);
    }

    @Test
    void getDoctorAppointments() {
        when(mockAppointmentRepository.findByDoctorID(appointment.getDoctorID(),null)).thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(appointment, appointment2))));

        Page<Appointment> appointmentDoctorPage = appointmentService.getDoctorAppointments(appointment.getDoctorID(),null);  // Get doctor page

        List<Appointment> doctorAppointments = appointmentDoctorPage.getContent();  // Get list of doctors from doctor page

        // Test doctor1 list length
        assertEquals(2, doctorAppointments.size());

        // Test doctor1 appointments match
        assertEquals(appointment, doctorAppointments.get(0));
        assertEquals(appointment2, doctorAppointments.get(1));

    }

    @Test
    void getPatientAppointments() {
        when(mockAppointmentRepository.findByPatientID(appointment.getPatientID(),null)).thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(appointment))));

        Page<Appointment> appointmentPatientPage = appointmentService.getPatientAppointments(appointment.getPatientID(),null);  // Get doctor page

        List<Appointment> patientAppointments = appointmentPatientPage.getContent();  // Get list of doctors from doctor page

        // Test patient1 list length
        assertEquals(1, patientAppointments.size());

        // Test patient1 match
        assertEquals(appointment, patientAppointments.get(0));

    }

    @Test
    void addAppointment() {
//        BDDMockito.given(mockAppointmentRepository.findByDoctorID(appointment.getAppointmentID())).willReturn(Optional.of(appointment));
        SpecialtyDTO specialtyDTO1 = new SpecialtyDTO();

        specialtyDTO1.setSpecialtyName("Cardiology");

        System.out.println(specialtyDTO1.getSpecialtyName());

//        Specialty tester = specialtyService.createSpecialty(specialtyDTO1);
//
//        verify(mockSpecialtyRepository).save(tester);

    }

    @Test
    void cancelAppointment() {
        when(mockAppointmentRepository.findByAppointmentID(appointment.getAppointmentID())).thenReturn(Optional.of(appointment));

        appointmentService.cancelAppointment(appointment.getAppointmentID());

        verify(mockAppointmentRepository).deleteById(appointment.getAppointmentID());
    }
}