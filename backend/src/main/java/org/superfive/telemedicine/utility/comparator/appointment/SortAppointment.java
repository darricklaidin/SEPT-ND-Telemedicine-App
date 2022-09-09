package org.superfive.telemedicine.utility.comparator.appointment;

import org.superfive.telemedicine.model.Appointment;

import java.util.Collections;
import java.util.List;

public class SortAppointment {
    private SortAppointment(){}

    public static void sortAppointment(String sortMethod, List<Appointment> appointments) {
        if (sortMethod != null) {
            String sortAttribute = sortMethod.split(",")[0];
            String sortDirection = "Asc";  // Default sort direction is ascending order

            if (sortMethod.split(",").length > 1) {
                // Ascending or descending
                sortDirection = sortMethod.split(",")[1];

            }

            // If attribute unknown, no sorting is done
            if (sortAttribute.equalsIgnoreCase("appointmentID")) {
                appointments.sort(new AppointmentIDComparator());
                if (sortDirection.equalsIgnoreCase("Desc")) {
                    Collections.reverse(appointments);
                }
            }
            else if (sortAttribute.equalsIgnoreCase("startDateTime")) {
                appointments.sort(new StartDateTimeComparator());
                if (sortDirection.equalsIgnoreCase("Desc")) {
                    Collections.reverse(appointments);
                }
            }
            else if (sortAttribute.equalsIgnoreCase("endDateTime")) {
                appointments.sort(new EndDateTimeComparator());
                if (sortDirection.equalsIgnoreCase("Desc")) {
                    Collections.reverse(appointments);
                }
            }
        }
    }
}
