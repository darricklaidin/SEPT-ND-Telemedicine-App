package org.superfive.telemedicine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Availability;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
    Page<Availability> findAllBy(Pageable pageable);

    Optional<Availability> findByAvailabilityID(int availabilityID);

    List<Availability> findByDoctorID(int doctorID);
}
