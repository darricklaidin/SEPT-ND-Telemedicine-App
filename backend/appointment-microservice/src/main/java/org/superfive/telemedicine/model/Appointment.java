package org.superfive.telemedicine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The Appointment entity stores a id, start date time, end date time, a doctor participant, and a
 * patient participant.
 */
@Entity
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentID;

    @NotNull
    private LocalDate date;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

    @NotBlank
    @Column(length = 9)
    private String appointmentStatus;  // UPCOMING, ONGOING, COMPLETED

    @NotNull
    private int doctorID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "patientID",
            referencedColumnName = "patientID",
            nullable = false
    )
    private Patient patient;

    public Appointment() {
    }


    public Appointment(int appointmentID, LocalDate date, LocalTime startTime, LocalTime endTime,
                       String appointmentStatus, int doctorID, Patient patient) {
        this.appointmentID = appointmentID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appointmentStatus = appointmentStatus;
        this.doctorID = doctorID;
        this.patient = patient;
    }
}
