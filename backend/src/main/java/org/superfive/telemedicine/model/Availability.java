package org.superfive.telemedicine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int availabilityID;

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "doctorID",
            referencedColumnName = "doctorID",
            nullable = false
    )
    private Doctor doctor;

    public Availability(int availabilityID, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Doctor doctor) {
        this.availabilityID = availabilityID;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctor = doctor;
    }

    public Availability() {}
}
