package sept.superfive.appointmentmicroservice.exception;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DoctorUnavailableException extends RuntimeException {
    public DoctorUnavailableException(int doctorID, DayOfWeek dayOfWeek, LocalTime appointmentStartTime,
                                      LocalTime appointmentEndTime) {
        super(String.format("Doctor ID: %s is not available on %s from %s - %s", doctorID, dayOfWeek,
                appointmentStartTime, appointmentEndTime));
    }
}
