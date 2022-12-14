package sept.superfive.authmicroservice.repository;

import sept.superfive.authmicroservice.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Page<Patient> findAllBy(Pageable pageable);

    Optional<Patient> findByUserID(int patientID);
}
