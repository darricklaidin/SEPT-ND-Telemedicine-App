package org.superfive.telemedicine.utility.comparator.appointment;

import org.superfive.telemedicine.model.Appointment;

import java.util.Calendar;
import java.util.Comparator;

public class AppointmentScheduleComparator implements Comparator<Appointment> {
    public int compare(Appointment appointment1, Appointment appointment2) {
        Calendar appointment1Schedule = appointment1.getAppointmentSchedule();
        Calendar appointment2Schedule = appointment2.getAppointmentSchedule();

        return appointment1Schedule.compareTo(appointment2Schedule);
    }
}
