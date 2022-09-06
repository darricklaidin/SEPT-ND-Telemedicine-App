package org.superfive.telemedicine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;

// Maybe use a class instead of a HashMap to return participants in the appointment
class Participants {
    private Doctor doctor;
    private Patient patient;

    public Participants(Doctor doctor, Patient patient) {
        this.doctor = doctor;
        this.patient = patient;
    }
}

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

    @NotBlank
    private Calendar appointmentSchedule;
    private String appointmentStatus;  // ONGOING, COMPLETED, UPCOMING

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "doctorID",
            referencedColumnName = "doctorID",
            nullable = false
    )
    private Doctor doctor;

    @JsonIgnore
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
