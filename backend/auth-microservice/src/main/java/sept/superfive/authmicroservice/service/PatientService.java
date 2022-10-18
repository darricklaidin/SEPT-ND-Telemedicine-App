package sept.superfive.authmicroservice.service;

import sept.superfive.authmicroservice.exception.ResourceNotFoundException;
import sept.superfive.authmicroservice.exception.ResourceAlreadyExistsException;
import sept.superfive.authmicroservice.model.Patient;
import sept.superfive.authmicroservice.repository.PatientRepository;
import sept.superfive.authmicroservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Page<Patient> getAllPatients(Pageable pageable) {
        return patientRepository.findAllBy(pageable);
    }

    @Transactional
    public Patient getPatientByID(int patientID) throws ResourceNotFoundException {
        return patientRepository.findByUserID(patientID)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "userID", patientID));
    }

    @Transactional
    public Patient updatePatient(int patientID, Patient patient) throws ResourceAlreadyExistsException {
        // Check that patient exists
        Patient oldPatient = this.getPatientByID(patientID);

        oldPatient.setFirstName(patient.getFirstName());
        oldPatient.setLastName(patient.getLastName());

        if (!oldPatient.getEmail().equals(patient.getEmail())) {
            if (Boolean.TRUE.equals(userRepository.existsByEmail(patient.getEmail()))) {
                throw new ResourceAlreadyExistsException("User", "email", patient.getEmail());
            }
            oldPatient.setEmail(patient.getEmail());
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (patient.getPassword() != null) {
            oldPatient.setPassword(passwordEncoder.encode(patient.getPassword()));
        }

        oldPatient.setDateOfBirth(patient.getDateOfBirth());
        oldPatient.setAccountStatus(patient.isAccountStatus());

        if (patient.getSymptoms() != null) {
            oldPatient.setSymptoms(patient.getSymptoms());
        }

        patientRepository.save(oldPatient);

        return oldPatient;
    }

    @Transactional
    public Patient deletePatient(int patientID) {
        Patient deletedPatient = this.getPatientByID(patientID);
        patientRepository.deleteById(patientID);
        return deletedPatient;
    }
}
