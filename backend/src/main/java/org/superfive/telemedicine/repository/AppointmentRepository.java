package org.superfive.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.superfive.telemedicine.model.Appointment;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findAllBy();

//    public List<Appointment> findAllByDate(Calendar calendar);

    @Query("FROM Appointment WHERE doctorid = ?1")
    List<Appointment> findByDoctorID(int doctorID);

    @Query("FROM Appointment WHERE patientid = ?1")
    List<Appointment> findByPatientID(int patientID);
}
