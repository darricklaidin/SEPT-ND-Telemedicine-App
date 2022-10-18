package sept.superfive.appointmentmicroservice.unit_test.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import sept.superfive.appointmentmicroservice.controller.AppointmentController;
import sept.superfive.appointmentmicroservice.exception.DoctorUnavailableException;
import sept.superfive.appointmentmicroservice.exception.EntityTimeClashException;
import sept.superfive.appointmentmicroservice.exception.InvalidTimeException;
import sept.superfive.appointmentmicroservice.model.Appointment;
import sept.superfive.appointmentmicroservice.service.AppointmentService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AppointmentControllerUnitTest {

    private AppointmentService mockAppointmentService;
    private AppointmentController appointmentController;

    private Appointment appointment1;
    private Appointment appointment2;

    private final List<Appointment> appointments = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mockAppointmentService = Mockito.mock(AppointmentService.class);
        appointmentController = new AppointmentController(mockAppointmentService);

        LocalDate date1 = LocalDate.of(2000, Month.JANUARY, 1);
        LocalTime startTime1 = LocalTime.of(10, 0, 0);
        LocalTime endTime1 = LocalTime.of(10, 30, 0);

        LocalDate date2 = LocalDate.of(2022, Month.DECEMBER, 23);
        LocalTime startTime2 = LocalTime.of(11, 0, 0);
        LocalTime endTime2 = LocalTime.of(11, 30, 0);

        appointment1 = new Appointment(1, date1, startTime1, endTime1, "COMPLETED", 1,1);
        appointment2 = new Appointment(2, date2, startTime2, endTime2, "UPCOMING", 1,2);

        appointments.add(appointment1);
        appointments.add(appointment2);
    }

    @Test
    void getAllAppointments() {
        when(mockAppointmentService.getAllAppointments(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(appointment1, appointment2))));

        Page<Appointment> appointmentPage = appointmentController.getAllAppointments(null).getBody();

        if (appointmentPage == null) {
            fail("Appointment page is null");
        }

        List<Appointment> retrievedAppointments = appointmentPage.getContent();

        // Test appointment list length
        assertEquals(appointments.size(), retrievedAppointments.size());

        // Test each appointment matches
        for (int i = 0; i < appointments.size(); i++) {
            assertEquals(appointments.get(i), retrievedAppointments.get(i));
        }
    }

    @Test
    void getAppointmentByID() {
        when(mockAppointmentService.getAppointmentByID(appointment1.getAppointmentID())).thenReturn(appointment1);
        Appointment testAppointment = appointmentController.getAppointmentByID(appointment1.getAppointmentID()).getBody();
        assertEquals(testAppointment, appointment1);
    }

    @Test
    void getDoctorAppointments() {
        when(mockAppointmentService.getDoctorAppointments(appointment1.getDoctorID(),null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(appointment1, appointment2))));

        Page<Appointment> appointmentDoctorPage = appointmentController
                .getDoctorAppointments(appointment1.getDoctorID(),null)
                .getBody();

        if (appointmentDoctorPage == null) {
            fail("Appointment page is null");
        }

        List<Appointment> retrievedDoctorAppointments = appointmentDoctorPage.getContent();

        // Test appointment1 list length
        assertEquals(2, retrievedDoctorAppointments.size());

        // Test appointment1 appointments match
        assertEquals(appointment1, retrievedDoctorAppointments.get(0));
        assertEquals(appointment2, retrievedDoctorAppointments.get(1));
    }

    @Test
    void getPatientAppointments() {
        when(mockAppointmentService.getPatientAppointments(appointment1.getPatientID(),null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(appointment1))));

        Page<Appointment> appointmentPatientPage = appointmentController
                .getPatientAppointments(appointment1.getPatientID(),null)
                .getBody();

        if (appointmentPatientPage == null) {
            fail("Appointment page is null");
        }

        List<Appointment> retrievedPatientAppointments = appointmentPatientPage.getContent();

        // Test appointment1 list length
        assertEquals(1, retrievedPatientAppointments.size());

        // Test appointment1 match
        assertEquals(appointment1, retrievedPatientAppointments.get(0));
    }

    @Test
    void addAppointment() {
        Appointment invalidAppointmentTimeRange = new Appointment(3,
                LocalDate.of(2000, Month.JANUARY, 1), LocalTime.of(10, 0, 0),
                LocalTime.of(9, 30, 0), "COMPLETED", 1,1);

        when(mockAppointmentService.addAppointment(invalidAppointmentTimeRange))
                .thenThrow(new InvalidTimeException(invalidAppointmentTimeRange.getStartTime(),
                        invalidAppointmentTimeRange.getEndTime()));

        // Add appointment with invalid time range
        assertThrows(InvalidTimeException.class, () -> appointmentController.addAppointment(invalidAppointmentTimeRange));

        Appointment appointment4 = new Appointment(4,
                LocalDate.of(2022, Month.OCTOBER, 10), LocalTime.of(12, 0, 0),
                LocalTime.of(12, 30, 0), "COMPLETED", 1,1);

        // Appointment 5 clashes with appointment 4
        Appointment appointment5 = new Appointment(5,
                LocalDate.of(2022, Month.OCTOBER, 10), LocalTime.of(12, 0, 0),
                LocalTime.of(12, 30, 0), "COMPLETED", 1,1);

        when(mockAppointmentService.addAppointment(appointment4))
                .thenThrow(new DoctorUnavailableException(appointment4.getDoctorID(),
                        appointment4.getDate().getDayOfWeek(), appointment4.getStartTime(), appointment4.getEndTime()));

        // Add appointment that is not within appointment's availability
        assertThrows(DoctorUnavailableException.class, () -> appointmentController.addAppointment(appointment4));

        when(mockAppointmentService.addAppointment(appointment5))
                .thenThrow(new EntityTimeClashException("Doctor", appointment5.getDoctorID(), appointment5.getStartTime(),
                appointment5.getEndTime(), appointment5.getStartTime(),
                        appointment5.getEndTime()));

        // Add appointment that is within appointment's availability but clashes with other appointments
        assertThrows(EntityTimeClashException.class, () -> appointmentController.addAppointment(appointment5));

        // Add appointment that is within appointment's availability and does not clash with other appointments
        Appointment appointment6 = new Appointment(6,
                LocalDate.of(2022, Month.OCTOBER, 10), LocalTime.of(15, 0, 0),
                LocalTime.of(15, 30, 0), "COMPLETED", 1,1);

        when(mockAppointmentService.addAppointment(appointment6)).thenReturn(appointment6);

        Appointment createdAppointment = appointmentController.addAppointment(appointment6).getBody();

        if (createdAppointment == null) {
            fail("Created appointment is null");
        }

        assertEquals(appointment6, createdAppointment);
        assertEquals(appointment6.getAppointmentID(), createdAppointment.getAppointmentID());
        assertEquals(appointment6.getDate(), createdAppointment.getDate());
        assertEquals(appointment6.getStartTime(), createdAppointment.getStartTime());
        assertEquals(appointment6.getEndTime(), createdAppointment.getEndTime());
        assertEquals(appointment6.getAppointmentStatus(), createdAppointment.getAppointmentStatus());
        assertEquals(appointment6.getPatientID(), createdAppointment.getPatientID());
        assertEquals(appointment6.getDoctorID(), createdAppointment.getDoctorID());

        when(mockAppointmentService.getAppointmentByID(appointment6.getAppointmentID())).thenReturn(appointment6);

        Appointment testAppointment = appointmentController.getAppointmentByID(appointment6.getAppointmentID()).getBody();
        assertEquals(testAppointment, appointment6);

        when(mockAppointmentService.getAllAppointments(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(List.of(appointment6))));
        // Check list size
        assertEquals(1, Objects.requireNonNull(appointmentController.getAllAppointments(null).getBody()).getContent().size());
        // Check appointment match
        assertEquals(appointment6, Objects.requireNonNull(appointmentController.getAllAppointments(null).getBody()).getContent().get(0));
        // Check each appointment attribute
        assertEquals(appointment6.getAppointmentID(), Objects.requireNonNull(appointmentController.getAllAppointments(null).getBody())
                .getContent().get(0).getAppointmentID());
        assertEquals(appointment6.getDate(), Objects.requireNonNull(appointmentController.getAllAppointments(null).getBody())
                .getContent().get(0).getDate());
        assertEquals(appointment6.getStartTime(), Objects.requireNonNull(appointmentController.getAllAppointments(null).getBody())
                .getContent().get(0).getStartTime());
        assertEquals(appointment6.getEndTime(), Objects.requireNonNull(appointmentController.getAllAppointments(null).getBody())
                .getContent().get(0).getEndTime());
        assertEquals(appointment6.getAppointmentStatus(), Objects.requireNonNull(appointmentController.getAllAppointments(null).getBody())
                .getContent().get(0).getAppointmentStatus());
        assertEquals(appointment6.getPatientID(), Objects.requireNonNull(appointmentController.getAllAppointments(null).getBody())
                .getContent().get(0).getPatientID());
        assertEquals(appointment6.getDoctorID(), Objects.requireNonNull(appointmentController.getAllAppointments(null).getBody())
                .getContent().get(0).getDoctorID());
    }

    @Test
    void cancelAppointment() {
        when(mockAppointmentService.getAppointmentByID(appointment1.getAppointmentID())).thenReturn(appointment1);

        when(mockAppointmentService.cancelAppointment(appointment1.getAppointmentID())).thenReturn(appointment1);

        Appointment deletedAppointment = appointmentController.cancelAppointment(appointment1.getAppointmentID()).getBody();

        when(mockAppointmentService.getAllAppointments(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(appointment2))));

        Page<Appointment> appointmentPage = appointmentController.getAllAppointments(null).getBody();

        if (appointmentPage == null) {
            fail("Appointment page is null");
        }

        List<Appointment> retrievedAppointments = appointmentPage.getContent();

        // Test appointment list length
        assertEquals(appointments.size() - 1, retrievedAppointments.size());

        // Test that appointment1 has been deleted
        assertEquals(appointment2, retrievedAppointments.get(0));

        // Test that deleted appointment matches appointment1
        assertEquals(appointment1, deletedAppointment);
    }

}