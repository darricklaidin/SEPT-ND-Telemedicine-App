package org.superfive.telemedicine.utility.comparator.appointment;

import org.superfive.telemedicine.model.Appointment;

import java.util.Comparator;

public class AppointmentIDComparator implements Comparator<Appointment> {
    public int compare(Appointment appointment1, Appointment appointment2) {
        int appointment1ID = appointment1.getAppointmentID();
        int appointment2ID = appointment2.getAppointmentID();

        return Integer.compare(appointment1ID, appointment2ID);
    }
}
