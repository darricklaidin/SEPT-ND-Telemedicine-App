package org.superfive.telemedicine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import java.util.Calendar;

/**
 * The Appointment entity stores a Calendar instance (date and time) of the appointment, a doctor participant, and a
 * patient participant.
 */
@Entity
@Getter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentID;

    private Calendar appointmentSchedule;

    @ManyToOne
    @JoinColumn(name = "doctorID", referencedColumnName = "doctorID", nullable = false)
    private Doctor doctor;

//    @ManyToOne
//    @JoinColumn(nullable = false)
//    private Patient patient;

    public Appointment() {}

    public Appointment(int appointmentID, Calendar appointmentSchedule, Doctor doctor) {
        this.appointmentID = appointmentID;
        this.appointmentSchedule = appointmentSchedule;
        this.doctor = doctor;
//        this.patient = patient;
    }

}
