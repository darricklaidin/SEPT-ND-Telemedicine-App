package org.superfive.telemedicine.utility.comparator.availability;

import org.superfive.telemedicine.model.Availability;

import java.util.Comparator;

public class StartTimeComparator implements Comparator<Availability> {
    public int compare(Availability availability1, Availability availability2) {
        return availability1.getStartTime().compareTo(availability2.getStartTime());
    }
}

