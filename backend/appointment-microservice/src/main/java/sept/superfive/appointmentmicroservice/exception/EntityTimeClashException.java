package sept.superfive.appointmentmicroservice.exception;

import java.time.LocalTime;

public class EntityTimeClashException extends RuntimeException {
    public EntityTimeClashException(String resourceName, int resourceID, LocalTime entityStartTime,
                                    LocalTime entityEndTime, LocalTime newAppointmentStartTime,
                                    LocalTime newAppointmentEndTime) {
        super("Unable to make appointment. There is a clash with "+ resourceName +" ID: " + resourceID + "'s schedule. " +
                resourceName + " schedule: " + entityStartTime + " - " + entityEndTime +
                " | New appointment schedule: "+ newAppointmentStartTime + " - " + newAppointmentEndTime);
    }
}
