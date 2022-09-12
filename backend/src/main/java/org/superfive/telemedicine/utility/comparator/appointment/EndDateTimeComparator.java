package org.superfive.telemedicine.utility.comparator.appointment;

import org.superfive.telemedicine.model.Appointment;

import java.time.LocalDateTime;
import java.util.Comparator;

public class EndDateTimeComparator implements Comparator<Appointment> {
    public int compare(Appointment appointment1, Appointment appointment2) {
        LocalDateTime appointment1EndDateTime = appointment1.getEndDateTime();
        LocalDateTime appointment2EndDateTime = appointment2.getEndDateTime();

        return appointment1EndDateTime.compareTo(appointment2EndDateTime);
    }
}
