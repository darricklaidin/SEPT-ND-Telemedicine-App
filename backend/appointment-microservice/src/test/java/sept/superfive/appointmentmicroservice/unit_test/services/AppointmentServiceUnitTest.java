package sept.superfive.appointmentmicroservice.unit_test.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import sept.superfive.appointmentmicroservice.exception.DoctorUnavailableException;
import sept.superfive.appointmentmicroservice.exception.EntityTimeClashException;
import sept.superfive.appointmentmicroservice.exception.InvalidTimeException;
import sept.superfive.appointmentmicroservice.model.Appointment;
import sept.superfive.appointmentmicroservice.model.Availability;
import sept.superfive.appointmentmicroservice.repository.AppointmentRepository;
import sept.superfive.appointmentmicroservice.repository.AvailabilityRepository;
import sept.superfive.appointmentmicroservice.service.AppointmentService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceUnitTest {

    private AppointmentRepository mockAppointmentRepository;
    private AvailabilityRepository mockAvailabilityRepository;
    private AppointmentService appointmentService;

    private Appointment appointment1;
    private Appointment appointment2;

    private final List<Appointment> appointments = new ArrayList<>();

    @BeforeEach
    void setUp() {
        mockAppointmentRepository = Mockito.mock(AppointmentRepository.class);
        mockAvailabilityRepository = Mockito.mock(AvailabilityRepository.class);
        appointmentService = new AppointmentService(mockAppointmentRepository, mockAvailabilityRepository);

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
        when(mockAppointmentRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(appointment1, appointment2))));

        Page<Appointment> appointmentPage = appointmentService.getAllAppointments(null);
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
        when(mockAppointmentRepository.findByAppointmentID(appointment1.getAppointmentID())).thenReturn(Optional.of(appointment1));
        Appointment testAppointment = appointmentService.getAppointmentByID(appointment1.getAppointmentID());
        assertEquals(testAppointment, appointment1);
    }

    @Test
    void getDoctorAppointments() {
        when(mockAppointmentRepository.findByDoctorID(appointment1.getDoctorID(),null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Arrays.asList(appointment1, appointment2))));

        Page<Appointment> appointmentDoctorPage = appointmentService.getDoctorAppointments(appointment1.getDoctorID(),null);
        List<Appointment> retrievedDoctorAppointments = appointmentDoctorPage.getContent();

        // Test appointment1 list length
        assertEquals(2, retrievedDoctorAppointments.size());

        // Test appointment1 appointments match
        assertEquals(appointment1, retrievedDoctorAppointments.get(0));
        assertEquals(appointment2, retrievedDoctorAppointments.get(1));
    }

    @Test
    void getPatientAppointments() {
        when(mockAppointmentRepository.findByPatientID(appointment1.getPatientID(),null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(appointment1))));

        Page<Appointment> appointmentPatientPage = appointmentService.getPatientAppointments(appointment1.getPatientID(),null);
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

        // Add appointment with invalid time range
        assertThrows(InvalidTimeException.class, () -> appointmentService.addAppointment(invalidAppointmentTimeRange));

        Appointment appointment4 = new Appointment(4,
                LocalDate.of(2022, Month.OCTOBER, 10), LocalTime.of(12, 0, 0),
                LocalTime.of(12, 30, 0), "COMPLETED", 1,1);

        // Appointment 5 clashes with appointment 4
        Appointment appointment5 = new Appointment(5,
                LocalDate.of(2022, Month.OCTOBER, 10), LocalTime.of(12, 0, 0),
                LocalTime.of(12, 30, 0), "COMPLETED", 1,1);

        // Mock no availabilities
        when(mockAvailabilityRepository.findByDoctorID(appointment4.getDoctorID(), null))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // Mock appointments for appointment
        when(mockAppointmentRepository.findByPatientID(appointment5.getPatientID(), null))
                .thenReturn(new PageImpl<>(new ArrayList<>(List.of(appointment4))));
        // Mock appointments for appointment
        when(mockAppointmentRepository.findByDoctorID(appointment5.getDoctorID(), null))
                .thenReturn(new PageImpl<>(new ArrayList<>(List.of(appointment4))));

        // Add appointment that is not within appointment's availability
        assertThrows(DoctorUnavailableException.class, () -> appointmentService.addAppointment(appointment4));

        // Mock an availability
        when(mockAvailabilityRepository.findByDoctorID(appointment4.getDoctorID(), null))
                .thenReturn(new PageImpl<>(new ArrayList<>(List.of(
                        new Availability(1, DayOfWeek.MONDAY,
                                LocalTime.of(10, 0, 0), LocalTime.of(18, 0, 0), 1))
                )));

        // Add appointment that is within appointment's availability but clashes with other appointments
        assertThrows(EntityTimeClashException.class, () -> appointmentService.addAppointment(appointment5));

        // Add appointment that is within appointment's availability and does not clash with other appointments
        Appointment appointment6 = new Appointment(6,
                LocalDate.of(2022, Month.OCTOBER, 10), LocalTime.of(15, 0, 0),
                LocalTime.of(15, 30, 0), "COMPLETED", 1,1);

        Appointment createdAppointment = appointmentService.addAppointment(appointment6);

        assertEquals(appointment6, createdAppointment);
        assertEquals(appointment6.getAppointmentID(), createdAppointment.getAppointmentID());
        assertEquals(appointment6.getDate(), createdAppointment.getDate());
        assertEquals(appointment6.getStartTime(), createdAppointment.getStartTime());
        assertEquals(appointment6.getEndTime(), createdAppointment.getEndTime());
        assertEquals(appointment6.getAppointmentStatus(), createdAppointment.getAppointmentStatus());
        assertEquals(appointment6.getPatientID(), createdAppointment.getPatientID());
        assertEquals(appointment6.getDoctorID(), createdAppointment.getDoctorID());

        when(mockAppointmentRepository.findByAppointmentID(appointment6.getAppointmentID())).thenReturn(Optional.of(appointment6));
        Appointment testAppointment = appointmentService.getAppointmentByID(appointment6.getAppointmentID());
        assertEquals(testAppointment, appointment6);

        when(mockAppointmentRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(List.of(appointment6))));
        // Check list size
        assertEquals(1, appointmentService.getAllAppointments(null).getContent().size());
        // Check appointment match
        assertEquals(appointment6, appointmentService.getAllAppointments(null).getContent().get(0));
        // Check each appointment attribute
        assertEquals(appointment6.getAppointmentID(), appointmentService.getAllAppointments(null).getContent().get(0).getAppointmentID());
        assertEquals(appointment6.getDate(), appointmentService.getAllAppointments(null).getContent().get(0).getDate());
        assertEquals(appointment6.getStartTime(), appointmentService.getAllAppointments(null).getContent().get(0).getStartTime());
        assertEquals(appointment6.getEndTime(), appointmentService.getAllAppointments(null).getContent().get(0).getEndTime());
        assertEquals(appointment6.getAppointmentStatus(), appointmentService.getAllAppointments(null).getContent().get(0).getAppointmentStatus());
        assertEquals(appointment6.getPatientID(), appointmentService.getAllAppointments(null).getContent().get(0).getPatientID());
        assertEquals(appointment6.getDoctorID(), appointmentService.getAllAppointments(null).getContent().get(0).getDoctorID());
    }

    @Test
    void cancelAppointment() {
        when(mockAppointmentRepository.findByAppointmentID(appointment1.getAppointmentID())).thenReturn(Optional.of(appointment1));

        doNothing().when(mockAppointmentRepository).deleteById(appointment1.getAppointmentID());

        Appointment deletedAppointment = appointmentService.cancelAppointment(appointment1.getAppointmentID());

        when(mockAppointmentRepository.findAllBy(null))
                .thenReturn(new PageImpl<>(new ArrayList<>(Collections.singletonList(appointment2))));

        Page<Appointment> appointmentPage = appointmentService.getAllAppointments(null);
        List<Appointment> retrievedAppointments = appointmentPage.getContent();

        // Test appointment list length
        assertEquals(appointments.size() - 1, retrievedAppointments.size());

        // Test that appointment1 has been deleted
        assertEquals(appointment2, retrievedAppointments.get(0));

        // Test that deleted appointment matches appointment1
        assertEquals(appointment1, deletedAppointment);
    }

}