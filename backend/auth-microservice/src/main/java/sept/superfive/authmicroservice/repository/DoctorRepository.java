package sept.superfive.authmicroservice.repository;

import sept.superfive.authmicroservice.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Page<Doctor> findAllBy(Pageable pageable);

    Optional<Doctor> findByUserID(int doctorId);
}
