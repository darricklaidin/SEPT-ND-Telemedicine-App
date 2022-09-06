package org.superfive.telemedicine.repository;

import org.superfive.telemedicine.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findAllBy();

    Optional<Doctor> findByUserID(int doctorId);

    List<Doctor> findAllBySpecialtySpecialtyID(Integer specialtyID);

    List<Doctor> findAllByAccountStatus(String accountStatus);

    List<Doctor> findAllBySpecialtySpecialtyIDAndAccountStatus(Integer specialtyID, String accountStatus);
}
