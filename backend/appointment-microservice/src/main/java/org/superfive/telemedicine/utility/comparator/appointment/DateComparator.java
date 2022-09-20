package org.superfive.telemedicine.utility.comparator.appointment;

import org.superfive.telemedicine.model.Appointment;

import java.util.Comparator;

public class DateComparator implements Comparator<Appointment> {
    public int compare(Appointment appointment1, Appointment appointment2) {
        return appointment1.getDate().compareTo(appointment2.getDate());
    }
}
