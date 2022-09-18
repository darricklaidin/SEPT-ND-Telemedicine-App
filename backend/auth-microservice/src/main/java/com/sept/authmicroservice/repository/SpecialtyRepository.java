package com.sept.authmicroservice.repository;

import com.sept.authmicroservice.model.RoleName;
import com.sept.authmicroservice.model.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
    Page<Specialty> findAllBy(Pageable pageable);

    Optional<Specialty> findBySpecialtyID(int specialtyId);

    Optional<Specialty> findBySpecialtyName(String specialtyName);

    Boolean existsBySpecialtyName(String name);
}
