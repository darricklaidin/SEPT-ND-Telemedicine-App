package sept.superfive.appointmentmicroservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sept.superfive.appointmentmicroservice.model.Appointment;

import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Page<Appointment> findAllBy(Pageable pageable);

    Optional<Appointment> findByAppointmentID(int appointmentID);

    Page<Appointment> findByDoctorID(int doctorID, Pageable pageable);

    Page<Appointment> findByPatientID(int patientID, Pageable pageable);
}
