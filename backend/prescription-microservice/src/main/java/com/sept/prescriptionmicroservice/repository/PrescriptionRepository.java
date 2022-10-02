package com.sept.prescriptionmicroservice.repository;

import com.sept.prescriptionmicroservice.model.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
    Page<Prescription> findAllBy(Pageable pageable);

    Page<Prescription> findByDoctorID(int doctorID, Pageable pageable);

    Page<Prescription> findByPatientID(int patientID, Pageable pageable);

    Optional<Prescription> findByPrescriptionID(int prescriptionID);

    //Boolean currentlyActive(String name);
}
