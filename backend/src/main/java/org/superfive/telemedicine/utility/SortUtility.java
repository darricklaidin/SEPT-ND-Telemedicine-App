package org.superfive.telemedicine.utility;

import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Availability;
import org.superfive.telemedicine.utility.comparator.appointment.AppointmentIDComparator;
import org.superfive.telemedicine.utility.comparator.appointment.EndDateTimeComparator;
import org.superfive.telemedicine.utility.comparator.appointment.PatientIDComparator;
import org.superfive.telemedicine.utility.comparator.appointment.StartDateTimeComparator;
import org.superfive.telemedicine.utility.comparator.availability.*;

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
            else if (sortAttribute.equalsIgnoreCase("startDateTime")) {
                appointments.sort(new StartDateTimeComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("endDateTime")) {
                appointments.sort(new EndDateTimeComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("doctorID")) {
                appointments.sort(new org.superfive.telemedicine.utility.comparator.appointment.DoctorIDComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("patientID")) {
                appointments.sort(new PatientIDComparator());
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
                availabilities.sort(new StartTimeComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("endTime")) {
                availabilities.sort(new EndTimeComparator());
            }
            else if (sortAttribute.equalsIgnoreCase("doctorID")) {
                availabilities.sort(new DoctorIDComparator());
            }

            if (sortDirection.equalsIgnoreCase("Desc")) {
                Collections.reverse(availabilities);
            }
        }
    }
}
