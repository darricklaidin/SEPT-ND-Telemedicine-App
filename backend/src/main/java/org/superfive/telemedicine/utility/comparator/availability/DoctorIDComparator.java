package org.superfive.telemedicine.utility.comparator.availability;

import org.superfive.telemedicine.model.Availability;

import java.util.Comparator;

public class DoctorIDComparator implements Comparator<Availability> {
    public int compare(Availability availability1, Availability availability2) {
        return Integer.compare(availability1.getDoctor().getUserID(), availability2.getDoctor().getUserID());
    }
}
