package com.sept.authmicroservice.integration_test;

import com.sept.authmicroservice.model.Specialty;
import com.sept.authmicroservice.payload.DoctorSignUp;
import com.sept.authmicroservice.payload.LoginRequest;
import com.sept.authmicroservice.repository.SpecialtyRepository;
import com.sept.authmicroservice.repository.UserRepository;
import com.sept.authmicroservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SpecialtyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    public void setup() {
        specialtyRepository.deleteAll();  // clear the database
        userRepository.deleteAll();

        // Add new specialties
        Specialty specialty1 = new Specialty("Cardiology");
        Specialty specialty2 = new Specialty("Surgery");

        specialtyRepository.save(specialty1);
        specialtyRepository.save(specialty2);

        Specialty specialty = specialtyRepository.findAllBy(null).getContent().get(0);

        authService.registerDoctor(
                new DoctorSignUp("Darrick", "Edbert", "darrick@g.com", "darrickedbert",
                        "2004-06-07", specialty.getSpecialtyID()));
        // Authenticate
        authService.authenticateUser(new LoginRequest("darrick@g.com", "darrickedbert"));
    }

    @Test
    void testGetAllSpecialties() throws Exception {
        mockMvc.perform(get("/api/specialties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content']", hasSize(2)))
                .andExpect(jsonPath("$['content'][0].specialtyName").value("Cardiology"))
                .andExpect(jsonPath("$['content'][1].specialtyName").value("Surgery"));
    }

    @Test
    void testGetSpecialtyById() throws Exception {
        Specialty specialty = specialtyRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(get("/api/specialties/" + specialty.getSpecialtyID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialtyName").value("Cardiology"));
    }

    @Test
    void testGetSpecialtyByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/specialties/-1"))
                .andExpect(status().isNotFound());
    }

    // Create
    @Test
    void testCreateSpecialty() throws Exception {
        mockMvc.perform(post("/api/specialties")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"specialtyName\": \"Dermatology\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialtyName").value("Dermatology"));
    }

    // Update
    @Test
    void testUpdateSpecialty() throws Exception {
        Specialty specialty = specialtyRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(put("/api/specialties/" + specialty.getSpecialtyID())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"specialtyName\": \"Dermatology\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialtyName").value("Dermatology"));
    }

    // Delete
    @Test
    void testDeleteSpecialty() throws Exception {
        Specialty specialty = specialtyRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(delete("/api/specialties/" + specialty.getSpecialtyID()))
                .andExpect(status().isOk());

        assertTrue(specialtyRepository.findBySpecialtyID(specialty.getSpecialtyID()).isEmpty());
    }


}
