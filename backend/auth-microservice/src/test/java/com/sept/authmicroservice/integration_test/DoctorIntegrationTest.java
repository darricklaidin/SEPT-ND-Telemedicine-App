package com.sept.authmicroservice.integration_test;

import com.sept.authmicroservice.model.Doctor;
import com.sept.authmicroservice.model.Specialty;
import com.sept.authmicroservice.payload.DoctorSignUp;
import com.sept.authmicroservice.payload.LoginRequest;
import com.sept.authmicroservice.repository.DoctorRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DoctorIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private AuthService authService;

    private Doctor doctor1;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();  // clear the database

        // Register new doctors
        doctor1 = new Doctor(0, "Darrick", "Edbert", "darrick@g.com",
                "darrick", LocalDate.of(2004, 6, 7), null, null);
        Doctor doctor2 = new Doctor(0, "Bryan", "Hong", "bryan@g.com",
                "bryan", LocalDate.of(1990, 1, 27), null, null);
        Doctor doctor3 = new Doctor(0, "Nimesh", "Silva", "nimesh@g.com",
                "nimesh", LocalDate.of(2001, 5, 16), null, null);

        List<Specialty> specialtyList = specialtyRepository.findAllBy(null).getContent();

        // Need to use auth service for password encryption
        authService.registerDoctor(
                new DoctorSignUp(doctor1.getFirstName(), doctor1.getLastName(), doctor1.getEmail(), doctor1.getPassword(),
                        doctor1.getDateOfBirth().toString(), specialtyList.get(0).getSpecialtyID()));
        authService.registerDoctor(
                new DoctorSignUp(doctor2.getFirstName(), doctor2.getLastName(), doctor2.getEmail(), doctor2.getPassword(),
                        doctor2.getDateOfBirth().toString(), specialtyList.get(1).getSpecialtyID()));
        authService.registerDoctor(
                new DoctorSignUp(doctor3.getFirstName(), doctor3.getLastName(), doctor3.getEmail(), doctor3.getPassword(),
                        doctor3.getDateOfBirth().toString(), specialtyList.get(1).getSpecialtyID()));

        // Authenticate
        authService.authenticateUser(new LoginRequest("darrick@g.com", "darrick"));
    }

    @Test
    void testGetAllDoctors() throws Exception {
        mockMvc.perform(get("/api/doctors/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content'].length()").value(3));
    }

    @Test
    void testGetDoctorById() throws Exception {
        Doctor doctor = doctorRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(get("/api/doctors/" + doctor.getUserID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(doctor.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(doctor.getLastName()))
                .andExpect(jsonPath("$.email").value(doctor.getEmail()))
                .andExpect(jsonPath("$.dateOfBirth").value(doctor.getDateOfBirth().toString()))
                .andExpect(jsonPath("$.specialty.specialtyID").value(doctor.getSpecialty().getSpecialtyID()))
                .andExpect(jsonPath("$.specialty.specialtyName").value(doctor.getSpecialty().getSpecialtyName()));
    }

    @Test
    void testGetDoctorByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/doctors/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateDoctor() throws Exception {
        Doctor newDoctor = new Doctor(0, "Lucas", "Gray", "lucas@g.com",
                "lucas", LocalDate.of(1192, 1, 27), null, null);

        Doctor doctor = doctorRepository.findAllBy(null).getContent().get(0);

        int specialtyID = specialtyRepository.findAllBy(null).getContent().get(1).getSpecialtyID();

        mockMvc.perform(put("/api/doctors/" + doctor.getUserID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"firstName\": \"" + newDoctor.getFirstName() + "\",\n" +
                                "    \"lastName\": \"" + newDoctor.getLastName() + "\",\n" +
                                "    \"email\": \"" + newDoctor.getEmail() + "\",\n" +
                                "    \"password\": \"" + newDoctor.getPassword() + "\",\n" +
                                "    \"dateOfBirth\": \"" + newDoctor.getDateOfBirth().toString() + "\",\n" +
                                "    \"specialty\" : {\n" +
                                "        \"specialtyID\": \"" + specialtyID + "\"\n" +
                                "    }\n" +
                                "}"))
                .andExpect(status().isOk());

        Optional<Doctor> updatedDoctor = doctorRepository.findById(doctor.getUserID());

        if (updatedDoctor.isPresent()) {
            assertEquals(newDoctor.getFirstName(), updatedDoctor.get().getFirstName());
            assertEquals(newDoctor.getLastName(), updatedDoctor.get().getLastName());
            assertEquals(newDoctor.getEmail(), updatedDoctor.get().getEmail());
            assertEquals(newDoctor.getDateOfBirth(), updatedDoctor.get().getDateOfBirth());
            assertEquals(specialtyID, updatedDoctor.get().getSpecialty().getSpecialtyID());

            assertNotEquals(doctor1.getFirstName(), updatedDoctor.get().getFirstName());
            assertNotEquals(doctor1.getLastName(), updatedDoctor.get().getLastName());
            assertNotEquals(doctor1.getEmail(), updatedDoctor.get().getEmail());
            assertNotEquals(doctor1.getDateOfBirth(), updatedDoctor.get().getDateOfBirth());
            assertNotEquals(doctor.getSpecialty().getSpecialtyID(), updatedDoctor.get().getSpecialty().getSpecialtyID());
        } else{
            fail("Doctor not found");
        }
    }

    @Test
    void testUpdateDoctorNotFound() throws Exception {
        Doctor newDoctor = new Doctor(0, "Lucas", "Gray", "lucas@g.com",
                "lucas", LocalDate.of(1192, 1, 27), null, null);

        mockMvc.perform(put("/api/doctors/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"firstName\": \"" + newDoctor.getFirstName() + "\",\n" +
                                "    \"lastName\": \"" + newDoctor.getLastName() + "\",\n" +
                                "    \"email\": \"" + newDoctor.getEmail() + "\",\n" +
                                "    \"password\": \"" + newDoctor.getPassword() + "\",\n" +
                                "    \"dateOfBirth\": \"" + newDoctor.getDateOfBirth().toString() + "\",\n" +
                                "    \"specialty\" : {\n" +
                                "        \"specialtyID\": \"" + "2" + "\"\n" +
                                "    }\n" +
                                "}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Resource Not Found"));
    }

    @Test
    void testDeleteDoctor() throws Exception {
        Doctor patient = doctorRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(delete("/api/doctors/" + patient.getUserID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Doctor> deletedDoctor = doctorRepository.findById(patient.getUserID());

        assertTrue(deletedDoctor.isEmpty());
    }

    @Test
    void testDeleteDoctorNotFound() throws Exception {
        mockMvc.perform(delete("/api/doctors/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Resource Not Found"));
    }
}
