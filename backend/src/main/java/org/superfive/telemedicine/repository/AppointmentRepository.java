package org.superfive.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.superfive.telemedicine.model.Appointment;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Set<Appointment> findAllBy();

    Optional<Appointment> findByAppointmentID(int appointmentID);

    Set<Appointment> findByAppointmentStatus(String appointmentStatus);

//    List<Appointment> findAllByDate(Calendar calendar);

//    @Query("FROM Appointment WHERE doctorid = ?1")
//    List<Appointment> findByDoctorID(int doctorID);
//
//    @Query("FROM Appointment WHERE patientid = ?1")
//    List<Appointment> findByPatientID(int patientID);
}
