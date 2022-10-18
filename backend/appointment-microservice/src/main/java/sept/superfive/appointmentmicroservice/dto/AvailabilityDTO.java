package sept.superfive.appointmentmicroservice.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
public class AvailabilityDTO {
    private int availabilityID;

    private int dayOfWeek;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    private int doctorID;

    public AvailabilityDTO(int availabilityID, int dayOfWeek, LocalTime startTime, LocalTime endTime, int doctorID) {
        this.availabilityID = availabilityID;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.doctorID = doctorID;
    }
}
