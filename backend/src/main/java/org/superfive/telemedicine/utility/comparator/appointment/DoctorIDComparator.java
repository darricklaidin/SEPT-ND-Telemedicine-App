package org.superfive.telemedicine.utility.comparator.appointment;

import org.superfive.telemedicine.model.Appointment;

import java.util.Comparator;

public class DoctorIDComparator implements Comparator<Appointment> {
    public int compare(Appointment appointment1, Appointment appointment2) {
        return Integer.compare(appointment1.getDoctor().getUserID(), appointment2.getDoctor().getUserID());
    }
}
