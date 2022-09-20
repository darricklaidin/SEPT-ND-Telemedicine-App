package org.superfive.telemedicine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.superfive.telemedicine.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    Page<Doctor> findAllBy(Pageable pageable);

    Optional<Doctor> findByUserID(int doctorId);

}
