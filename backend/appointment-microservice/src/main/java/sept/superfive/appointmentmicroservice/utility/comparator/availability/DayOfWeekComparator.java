package sept.superfive.appointmentmicroservice.utility.comparator.availability;

import sept.superfive.appointmentmicroservice.model.Availability;

import java.util.Comparator;

public class DayOfWeekComparator implements Comparator<Availability> {
    public int compare(Availability availability1, Availability availability2) {
        return availability1.getDayOfWeek().compareTo(availability2.getDayOfWeek());
    }
}
