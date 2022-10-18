package org.superfive.telemedicine.integration_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Availability;
import org.superfive.telemedicine.repository.AppointmentRepository;
import org.superfive.telemedicine.repository.AvailabilityRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AppointmentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AvailabilityRepository availabilityRepository;

    @BeforeEach
    public void setupBeforeEach() {
        appointmentRepository.deleteAll();

        Appointment appointment1 = new Appointment(-1, LocalDate.of(2022, 10, 18),
                LocalTime.of(8, 0, 0), LocalTime.of(8, 30, 0),
                "UPCOMING", 1, 2);

        Appointment appointment2 = new Appointment(-1, LocalDate.of(2022, 10, 19),
                LocalTime.of(12, 0, 0), LocalTime.of(12, 30, 0),
                "UPCOMING", 1, 2);

        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);

        availabilityRepository.deleteAll();

        // set up availabilities for tuesday and wednesday
        Availability availability1 = new Availability(-1, DayOfWeek.TUESDAY, LocalTime.of(8, 0, 0),
                LocalTime.of(20, 0, 0), 1);
        Availability availability2 = new Availability(-1, DayOfWeek.WEDNESDAY, LocalTime.of(8, 0, 0),
                LocalTime.of(20, 0, 0), 1);

        availabilityRepository.save(availability1);
        availabilityRepository.save(availability2);
    }

    @Test
    void testGetAllAppointments() throws Exception {
        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content'].length()").value(2));
    }

    @Test
    void testGetAppointmentByID() throws Exception {
        Appointment appointment = appointmentRepository.findAllBy(null).getContent().get(0);


        mockMvc.perform(get("/api/appointments/" + appointment.getAppointmentID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentID").value(appointment.getAppointmentID()))
                .andExpect(jsonPath("$.date").value(appointment.getDate().toString()))
                .andExpect(jsonPath("$.startTime").value(appointment.getStartTime().format(DateTimeFormatter.ISO_TIME)))
                .andExpect(jsonPath("$.endTime").value(appointment.getEndTime().format(DateTimeFormatter.ISO_TIME)))
                .andExpect(jsonPath("$.appointmentStatus").value(appointment.getAppointmentStatus()))
                .andExpect(jsonPath("$.patientID").value(appointment.getPatientID()))
                .andExpect(jsonPath("$.doctorID").value(appointment.getDoctorID()));
    }

    @Test
    void testGetDoctorAppointments() throws Exception {
        mockMvc.perform(get("/api/appointments/doctor/" + "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content'].length()").value(2));
    }

    @Test
    void testGetPatientAppointments() throws Exception {
        mockMvc.perform(get("/api/appointments/patient/" + "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content'].length()").value(2));
    }

    @Test
    void testCreateAppointmentNotInDoctorAvailability() throws Exception {
        String appointment1 = "{\n" +
                "    \"date\": \"2022-10-18\",\n" +
                "    \"startTime\": \"06:00:00\",\n" +
                "    \"endTime\": \"06:30:00\",\n" +
                "    \"appointmentStatus\": \"UPCOMING\",\n" +
                "    \"patientID\": 2,\n" +
                "    \"doctorID\": 1\n" +
                "}";

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(appointment1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Doctor Unavailable"));

        String appointment2 = "{\n" +
                "    \"date\": \"2022-10-20\",\n" +
                "    \"startTime\": \"06:00:00\",\n" +
                "    \"endTime\": \"06:30:00\",\n" +
                "    \"appointmentStatus\": \"UPCOMING\",\n" +
                "    \"patientID\": 2,\n" +
                "    \"doctorID\": 1\n" +
                "}";

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(appointment2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Doctor Unavailable"));
    }

    @Test
    void testCreateAppointmentTimeClash() throws Exception {
        String appointment = "{\n" +
                "    \"date\": \"2022-10-18\",\n" +
                "    \"startTime\": \"08:00:00\",\n" +
                "    \"endTime\": \"08:30:00\",\n" +
                "    \"appointmentStatus\": \"UPCOMING\",\n" +
                "    \"patientID\": 2,\n" +
                "    \"doctorID\": 1\n" +
                "}";

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(appointment))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Time Clash"));
    }

    @Test
    void testCreateAppointmentSuccess() throws Exception {
        // Clash with existing appointment
        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"date\": \"2022-10-18\",\n" +
                        "    \"startTime\": \"11:00:00\",\n" +
                        "    \"endTime\": \"11:30:00\",\n" +
                        "    \"appointmentStatus\": \"UPCOMING\",\n" +
                        "    \"patientID\": 2,\n" +
                        "    \"doctorID\": 1\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2022-10-18"))
                .andExpect(jsonPath("$.startTime").value("11:00:00"))
                .andExpect(jsonPath("$.endTime").value("11:30:00"))
                .andExpect(jsonPath("$.appointmentStatus").value("UPCOMING"))
                .andExpect(jsonPath("$.patientID").value(2))
                .andExpect(jsonPath("$.doctorID").value(1));

        assertEquals(3, appointmentRepository.findAllBy(null).getContent().size());
    }

    @Test
    void testDeleteAppointment() throws Exception {
        Appointment appointment = appointmentRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(delete("/api/appointments/" + appointment.getAppointmentID()))
                .andExpect(status().isOk());

        assertEquals(1, appointmentRepository.findAllBy(null).getContent().size());
    }

}
