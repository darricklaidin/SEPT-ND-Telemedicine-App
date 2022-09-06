package org.superfive.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.superfive.telemedicine.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    List<Patient> findAllBy();

    Optional<Patient> findByUserID(int patientID);

    List<Patient> findAllByAccountStatus(String accountStatus);
}
