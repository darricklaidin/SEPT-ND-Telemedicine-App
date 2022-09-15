package org.superfive.telemedicine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.superfive.telemedicine.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    Page<Medicine> findAllBy(Pageable pageable);
    Optional<Medicine> findBySpecialtyID(int medicineId);
    //Created by Java, returns an Optional Object. Hence need to use Optional.
}
