package com.sept.authmicroservice.integration_test;

import com.sept.authmicroservice.model.Doctor;
import com.sept.authmicroservice.model.User;
import com.sept.authmicroservice.repository.DoctorRepository;
import com.sept.authmicroservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthIntegrationTest {

    /*
    * Register new patients
    * Register new doctors
    */

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();  // clear the database
    }


    @Test
    void registerNewPatient() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"firstName\": \"Darrick\",\n" +
                                "    \"lastName\": \"Edbert\",\n" +
                                "    \"email\": \"darrick@g.com\",\n" +
                                "    \"password\": \"darrickedbert\",\n" +
                                "    \"dateOfBirth\": \"2004-06-07\"\n" +
                                "}"))
                .andExpect(status().isOk());

        // Check that it has been added in user
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);

        assertEquals(1, users.size());
        assertEquals("Darrick", users.get(0).getFirstName());
        assertEquals("Edbert", users.get(0).getLastName());
        assertEquals("darrick@g.com", users.get(0).getEmail());
        assertEquals(LocalDate.of(2004, 6, 7), users.get(0).getDateOfBirth());
    }

    @Test
    void registerNewDoctor() throws Exception {
        mockMvc.perform(post("/api/auth/signup-doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"firstName\": \"Darrick\",\n" +
                                "    \"lastName\": \"Edbert\",\n" +
                                "    \"email\": \"darrick@g.com\",\n" +
                                "    \"password\": \"darrickedbert\",\n" +
                                "    \"dateOfBirth\": \"2004-06-07\",\n" +
                                "    \"specialtyID\": \"1\"\n" +
                                "}"))
                .andExpect(status().isOk());

        // Check that it has been added in user
        List<Doctor> doctors = new ArrayList<>(doctorRepository.findAllBy(null).getContent());

        assertEquals(1, doctors.size());
        assertEquals("Darrick", doctors.get(0).getFirstName());
        assertEquals("Edbert", doctors.get(0).getLastName());
        assertEquals("darrick@g.com", doctors.get(0).getEmail());
        assertEquals(LocalDate.of(2004, 6, 7), doctors.get(0).getDateOfBirth());
        assertEquals(1, doctors.get(0).getSpecialty().getSpecialtyID());
    }

}
