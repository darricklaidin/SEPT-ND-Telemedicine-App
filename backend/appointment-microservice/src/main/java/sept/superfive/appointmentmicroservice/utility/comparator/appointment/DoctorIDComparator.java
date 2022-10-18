package sept.superfive.appointmentmicroservice.utility.comparator.appointment;

import sept.superfive.appointmentmicroservice.model.Appointment;

import java.util.Comparator;

public class DoctorIDComparator implements Comparator<Appointment> {
    public int compare(Appointment appointment1, Appointment appointment2) {
        return Integer.compare(appointment1.getDoctorID(), appointment2.getDoctorID());
    }
}
