package org.superfive.telemedicine.utility.comparator.appointment;

import org.superfive.telemedicine.model.Appointment;

import java.time.LocalDateTime;
import java.util.Comparator;

public class EndDateTimeComparator implements Comparator<Appointment> {
    public int compare(Appointment appointment1, Appointment appointment2) {
        LocalDateTime appointment1Schedule = appointment1.getEndDateTime();
        LocalDateTime appointment2Schedule = appointment2.getEndDateTime();

        return appointment1Schedule.compareTo(appointment2Schedule);
    }
}
