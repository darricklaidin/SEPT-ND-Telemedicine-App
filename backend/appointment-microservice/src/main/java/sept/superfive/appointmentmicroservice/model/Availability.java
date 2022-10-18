package sept.superfive.appointmentmicroservice.model;

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

    @NotNull
    private int doctorID;

    public Availability(int availabilityID, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, int doctorID) {
        this.availabilityID = availabilityID;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctorID = doctorID;
    }

    public Availability() {
    }
}
