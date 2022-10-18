package sept.superfive.appointmentmicroservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sept.superfive.appointmentmicroservice.model.Availability;

import java.time.DayOfWeek;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
    Page<Availability> findAllBy(Pageable pageable);

    Optional<Availability> findByAvailabilityID(int availabilityID);

    Page<Availability> findByDoctorID(int doctorID, Pageable pageable);

    Optional<Availability> findByDoctorIDAndDayOfWeek(int doctorID, DayOfWeek dayOfWeek);
}
