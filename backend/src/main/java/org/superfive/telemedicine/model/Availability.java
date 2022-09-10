package org.superfive.telemedicine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int availabilityID;

    @NotEmpty
    private DayOfWeek dayOfWeek;

    @NotEmpty
    private LocalTime startTime;

    @NotEmpty
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
