package sept.superfive.authmicroservice.integration_test;

import sept.superfive.authmicroservice.model.Patient;
import sept.superfive.authmicroservice.payload.LoginRequest;
import sept.superfive.authmicroservice.payload.SignUpRequest;
import sept.superfive.authmicroservice.repository.PatientRepository;
import sept.superfive.authmicroservice.repository.UserRepository;
import sept.superfive.authmicroservice.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PatientIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();  // clear the database

        // Register new patients
        Patient patient1 = new Patient(0, "Darrick", "Edbert", "darrick@g.com",
                "darrick", LocalDate.of(2004, 6, 7), null);
        Patient patient2 = new Patient(0, "Bryan", "Hong", "bryan@g.com",
                "bryan", LocalDate.of(1990, 1, 27), null);
        Patient patient3 = new Patient(0, "Nimesh", "Silva", "nimesh@g.com",
                "nimesh", LocalDate.of(2001, 5, 16), null);

        // Need to use auth service for password encryption
        authService.registerPatient(
                new SignUpRequest(patient1.getFirstName(), patient1.getLastName(), patient1.getEmail(), patient1.getPassword(),
                        patient1.getDateOfBirth().toString()));
        authService.registerPatient(
                new SignUpRequest(patient2.getFirstName(), patient2.getLastName(), patient2.getEmail(), patient2.getPassword(),
                        patient2.getDateOfBirth().toString()));
        authService.registerPatient(
                new SignUpRequest(patient3.getFirstName(), patient3.getLastName(), patient3.getEmail(), patient3.getPassword(),
                        patient3.getDateOfBirth().toString()));

        // Authenticate
        authService.authenticateUser(new LoginRequest("darrick@g.com", "darrick"));
    }

    @Test
    void testGetAllPatients() throws Exception {
        mockMvc.perform(get("/api/patients/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['content'].length()").value(3));
    }

    @Test
    void testGetPatientById() throws Exception {
        Patient patient = patientRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(get("/api/patients/" + patient.getUserID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(patient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(patient.getLastName()))
                .andExpect(jsonPath("$.email").value(patient.getEmail()))
                .andExpect(jsonPath("$.dateOfBirth").value(patient.getDateOfBirth().toString()));
    }

    @Test
    void testGetPatientByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/patients/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdatePatient() throws Exception {
        Patient newPatient = new Patient(0, "Lucas", "Gray", "lucas@g.com",
                "lucas", LocalDate.of(1192, 1, 27), null);
        newPatient.setSymptoms("headache");


        Patient patient = patientRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(put("/api/patients/" + patient.getUserID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"firstName\": \"" + newPatient.getFirstName() + "\",\n" +
                                "    \"lastName\": \"" + newPatient.getLastName() + "\",\n" +
                                "    \"email\": \"" + newPatient.getEmail() + "\",\n" +
                                "    \"password\": \"" + newPatient.getPassword() + "\",\n" +
                                "    \"dateOfBirth\": \"" + newPatient.getDateOfBirth().toString() + "\",\n" +
                                "    \"symptoms\": \"" + newPatient.getSymptoms() + "\"\n" +
                                "}"))
                .andExpect(status().isOk());

        Optional<Patient> updatedPatient = patientRepository.findById(patient.getUserID());

        if (updatedPatient.isPresent()) {
            assertEquals(newPatient.getFirstName(), updatedPatient.get().getFirstName());
            assertEquals(newPatient.getLastName(), updatedPatient.get().getLastName());
            assertEquals(newPatient.getEmail(), updatedPatient.get().getEmail());
            assertEquals(newPatient.getDateOfBirth(), updatedPatient.get().getDateOfBirth());
            assertEquals(newPatient.getSymptoms(), updatedPatient.get().getSymptoms());
        } else{
            fail("Patient not found");
        }
    }

    @Test
    void testUpdatePatientNotFound() throws Exception {
        Patient newPatient = new Patient(0, "Lucas", "Gray", "lucas@g.com",
                "lucas", LocalDate.of(1192, 1, 27), null);

        mockMvc.perform(put("/api/patients/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"firstName\": \"" + newPatient.getFirstName() + "\",\n" +
                                "    \"lastName\": \"" + newPatient.getLastName() + "\",\n" +
                                "    \"email\": \"" + newPatient.getEmail() + "\",\n" +
                                "    \"password\": \"" + newPatient.getPassword() + "\",\n" +
                                "    \"dateOfBirth\": \"" + newPatient.getDateOfBirth().toString() + "\",\n" +
                                "    \"symptoms\": \"" + newPatient.getSymptoms() + "\"\n" +
                                "}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Resource Not Found"));
    }

    @Test
    void testDeletePatient() throws Exception {
        Patient patient = patientRepository.findAllBy(null).getContent().get(0);

        mockMvc.perform(delete("/api/patients/" + patient.getUserID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Patient> deletedPatient = patientRepository.findById(patient.getUserID());

        assertTrue(deletedPatient.isEmpty());
    }

    @Test
    void testDeletePatientNotFound() throws Exception {
        mockMvc.perform(delete("/api/patients/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$['message']").value("Resource Not Found"));
    }


}
