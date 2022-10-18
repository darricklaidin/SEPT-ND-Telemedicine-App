package sept.superfive.appointmentmicroservice.utility.comparator.availability;

import sept.superfive.appointmentmicroservice.model.Availability;

import java.util.Comparator;

public class AvailabilityIDComparator implements Comparator<Availability> {
    public int compare(Availability availability1, Availability availability2) {
        return Integer.compare(availability1.getAvailabilityID(), availability2.getAvailabilityID());
    }
}
