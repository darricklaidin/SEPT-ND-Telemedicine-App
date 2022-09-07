package org.superfive.telemedicine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;

/**
 * The Appointment entity stores a Calendar instance (date and time) of the appointment, a doctor participant, and a
 * patient participant.
 */
@Entity
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentID;

    private Calendar appointmentSchedule;
    @NotBlank
    private String appointmentStatus;  // ONGOING, COMPLETED, UPCOMING

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "doctorID",
            referencedColumnName = "doctorID",
            nullable = false
    )
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name= "patientID",
            referencedColumnName = "patientID",
            nullable = false
    )
    private Patient patient;

    public Appointment() {}

    public Appointment(int appointmentID, Calendar appointmentSchedule, String appointmentStatus, Doctor doctor,
                       Patient patient) {
        this.appointmentID = appointmentID;
        this.appointmentSchedule = appointmentSchedule;
        this.appointmentStatus = appointmentStatus;
        this.doctor = doctor;
        this.patient = patient;
    }

}
