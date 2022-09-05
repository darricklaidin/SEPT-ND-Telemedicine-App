package org.superfive.telemedicine.repository;

import org.superfive.telemedicine.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findById(int doctorId);

    List<Doctor> findAllBy();

}
