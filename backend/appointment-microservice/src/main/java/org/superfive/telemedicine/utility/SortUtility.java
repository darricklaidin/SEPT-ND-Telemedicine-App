package org.superfive.telemedicine.utility;

import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Availability;
import org.superfive.telemedicine.utility.comparator.appointment.*;
import org.superfive.telemedicine.utility.comparator.availability.AvailabilityIDComparator;
import org.superfive.telemedicine.utility.comparator.availability.DayOfWeekComparator;

import java.util.Collections;
import java.util.List;

public class SortUtility {
    private SortUtility(){}

    public static void sortAppointments(String sortMethod, List<Appointment> appointments) {
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
            }
            else if (sortAttribute.equalsIgnoreCase("date")) {
                appointments.sort(new DateComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("startTime")) {
                appointments.sort(new StartTimeComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("endTime")) {
                appointments.sort(new EndTimeComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("doctorID")) {
                appointments.sort(new org.superfive.telemedicine.utility.comparator.appointment.DoctorIDComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("patientID")) {
                appointments.sort(new PatientIDComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("dateTime")) {
                appointments.sort(new DateComparator().thenComparing(new StartTimeComparator()));
            }

            if (sortDirection.equalsIgnoreCase("Desc")) {
                Collections.reverse(appointments);
            }
        }
    }

    public static void sortAvailabilities(String sortMethod, List<Availability> availabilities) {
        if (sortMethod != null) {
            String sortAttribute = sortMethod.split(",")[0];
            String sortDirection = "Asc";  // Default sort direction is ascending order

            if (sortMethod.split(",").length > 1) {
                // Ascending or descending
                sortDirection = sortMethod.split(",")[1];
            }

            // If attribute unknown, no sorting is done
            if (sortAttribute.equalsIgnoreCase("availabilityID")) {
                availabilities.sort(new AvailabilityIDComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("dayOfWeek")) {
                availabilities.sort(new DayOfWeekComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("startTime")) {
                availabilities.sort(new org.superfive.telemedicine.utility.comparator.availability.StartTimeComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("endTime")) {
                availabilities.sort(new org.superfive.telemedicine.utility.comparator.availability.EndTimeComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("doctorID")) {
                availabilities.sort(new org.superfive.telemedicine.utility.comparator.availability.DoctorIDComparator());
            }

            if (sortDirection.equalsIgnoreCase("Desc")) {
                Collections.reverse(availabilities);
            }
        }
    }
}
