package sept.superfive.appointmentmicroservice.utility.comparator.appointment;

import sept.superfive.appointmentmicroservice.model.Appointment;

import java.util.Comparator;

public class StartTimeComparator implements Comparator<Appointment> {
    public int compare(Appointment appointment1, Appointment appointment2) {
        return appointment1.getStartTime().compareTo(appointment2.getStartTime());
    }
}
