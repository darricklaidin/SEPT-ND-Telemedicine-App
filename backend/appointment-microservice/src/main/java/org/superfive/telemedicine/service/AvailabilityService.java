package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.dto.AvailabilityDTO;
import org.superfive.telemedicine.exception.InvalidTimeException;
import org.superfive.telemedicine.exception.ResourceAlreadyExistsException;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Availability;
import org.superfive.telemedicine.repository.AvailabilityRepository;

import javax.transaction.Transactional;
import java.time.DayOfWeek;

@Service
public class AvailabilityService {
    private final AvailabilityRepository availabilityRepository;
    private final String RESOURCE_NAME = "Availability";

    @Autowired
    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    public Page<Availability> getAllAvailabilities(Pageable pageable) {
        return availabilityRepository.findAllBy(pageable);
    }

    public Availability getAvailabilityByID(int availabilityID) throws ResourceNotFoundException {
        return availabilityRepository.findByAvailabilityID(availabilityID)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "availabilityID", availabilityID));
    }

    // Get a doctor's availabilities
    public Page<Availability> getDoctorAvailabilities(int doctorID, Pageable pageable) {
        return availabilityRepository.findByDoctorID(doctorID, pageable);
    }

    @Transactional
    public Availability addAvailability(AvailabilityDTO availability) throws ResourceAlreadyExistsException,
            InvalidTimeException {

        // Ensure no id conflict
        try {
            this.getAvailabilityByID(availability.getAvailabilityID());
            throw new ResourceAlreadyExistsException(RESOURCE_NAME, "availabilityID", availability.getAvailabilityID());
        } catch (ResourceNotFoundException exception) {
            // Id does not exist yet, continue...
        }

        // Ensure availability schedule does not already exist in the database for the particular doctor
        if (availabilityRepository.findByDoctorIDAndDayOfWeek(availability.getDoctorID(), DayOfWeek.of(availability.getDayOfWeek()))
                .isPresent()) {
            throw new ResourceAlreadyExistsException(RESOURCE_NAME, "doctorID and dayOfWeek",
                    availability.getDoctorID() + " and " + availability.getDayOfWeek());
        }

        // Ensure start time comes before end time
        if (availability.getStartTime().isAfter(availability.getEndTime())) {
            throw new InvalidTimeException(availability.getStartTime(), availability.getEndTime());
        }

        Availability temp = new Availability(
                availability.getAvailabilityID(),
                DayOfWeek.of(availability.getDayOfWeek()),
                availability.getStartTime(),
                availability.getEndTime(),
                availability.getDoctorID()
        );

        availabilityRepository.save(temp);

        return temp;
    }

    @Transactional
    public Availability rescheduleAvailability(AvailabilityDTO availability, int availabilityID) throws InvalidTimeException {

        Availability updatedAvailability = this.getAvailabilityByID(availabilityID);

        updatedAvailability.setDayOfWeek(DayOfWeek.of(availability.getDayOfWeek()));
        updatedAvailability.setStartTime(availability.getStartTime());
        updatedAvailability.setEndTime(availability.getEndTime());

        // Ensure times will still be valid after update
        if (updatedAvailability.getStartTime().isAfter(updatedAvailability.getEndTime())) {
            throw new InvalidTimeException(updatedAvailability.getStartTime(), updatedAvailability.getEndTime());
        }

        availabilityRepository.save(updatedAvailability);

        return updatedAvailability;
    }

    @Transactional
    public Availability deleteAvailability(int availabilityID) {
        Availability deletedAvailability = this.getAvailabilityByID(availabilityID);

        availabilityRepository.deleteById(availabilityID);

        return deletedAvailability;
    }
}
