package sept.superfive.appointmentmicroservice.integration_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sept.superfive.appointmentmicroservice.model.Availability;
import sept.superfive.appointmentmicroservice.repository.AvailabilityRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AvailabilityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @BeforeEach
    public void setupBeforeEach() {
        availabilityRepository.deleteAll();

        Availability availability1 = new Availability(-1, DayOfWeek.TUESDAY, LocalTime.of(8, 0, 0),
                LocalTime.of(20, 0, 0), 1);
        Availability availability2 = new Availability(-1, DayOfWeek.WEDNESDAY, LocalTime.of(9, 0, 0),
                LocalTime.of(17, 0, 0), 1);

        availabilityRepository.save(availability1);
        availabilityRepository.save(availability2);
    }

    @Test
    void testGetAllAvailabilities() throws Exception {
        mockMvc.perform(get("/api/availabilities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content'].length()").value(2));
    }

    @Test
    void testGetAvailabilityByID() throws Exception {
        Availability availability = availabilityRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(get("/api/availabilities/" + availability.getAvailabilityID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availabilityID").value(availability.getAvailabilityID()))
                .andExpect(jsonPath("$.dayOfWeek").value(availability.getDayOfWeek().toString()))
                .andExpect(jsonPath("$.startTime").value(availability.getStartTime().format(DateTimeFormatter.ISO_TIME)))
                .andExpect(jsonPath("$.endTime").value(availability.getEndTime().format(DateTimeFormatter.ISO_TIME)))
                .andExpect(jsonPath("$.doctorID").value(availability.getDoctorID()));
    }

    @Test
    void testGetDoctorAvailabilities() throws Exception {
        mockMvc.perform(get("/api/availabilities/doctor/" + "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content'].length()").value(2));
    }

    @Test
    void testCreateAvailabilityAlreadyExists() throws Exception {
        // Availability already exists
        mockMvc.perform(post("/api/availabilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"dayOfWeek\": \"" + DayOfWeek.TUESDAY.getValue() + "\",\n" +
                                "    \"startTime\": \"08:00:00\",\n" +
                                "    \"endTime\": \"20:30:00\",\n" +
                                "    \"doctorID\": 1\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Resource Already Exists"));
    }

    @Test
    void testCreateAvailability() throws Exception {
        mockMvc.perform(post("/api/availabilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"dayOfWeek\": \"" + DayOfWeek.THURSDAY.getValue() + "\",\n" +
                                "    \"startTime\": \"08:00:00\",\n" +
                                "    \"endTime\": \"20:30:00\",\n" +
                                "    \"doctorID\": 1\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dayOfWeek").value("THURSDAY"))
                .andExpect(jsonPath("$.startTime").value("08:00:00"))
                .andExpect(jsonPath("$.endTime").value("20:30:00"))
                .andExpect(jsonPath("$.doctorID").value(1));

        assertEquals(3, availabilityRepository.findAllBy(null).getContent().size());
    }

    @Test
    void testDeleteAvailability() throws Exception {
        Availability availability = availabilityRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(delete("/api/availabilities/" + availability.getAvailabilityID()))
                .andExpect(status().isOk());

        assertEquals(1, availabilityRepository.findAllBy(null).getContent().size());
    }


}
