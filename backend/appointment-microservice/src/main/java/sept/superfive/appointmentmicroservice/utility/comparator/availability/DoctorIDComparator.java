package sept.superfive.appointmentmicroservice.utility.comparator.availability;

import sept.superfive.appointmentmicroservice.model.Availability;

import java.util.Comparator;

public class DoctorIDComparator implements Comparator<Availability> {
    public int compare(Availability availability1, Availability availability2) {
        return Integer.compare(availability1.getDoctorID(), availability2.getDoctorID());
    }
}
