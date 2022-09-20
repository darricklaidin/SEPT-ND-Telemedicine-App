package org.superfive.telemedicine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.superfive.telemedicine.model.Patient;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Page<Patient> findAllBy(Pageable pageable);

    Optional<Patient> findByUserID(int patientID);

}
