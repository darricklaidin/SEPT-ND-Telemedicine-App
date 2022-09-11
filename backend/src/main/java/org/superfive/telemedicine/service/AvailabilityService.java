package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.exception.InvalidTimeException;
import org.superfive.telemedicine.exception.ResourceAlreadyExistsException;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Availability;
import org.superfive.telemedicine.repository.AvailabilityRepository;

@Service
public class AvailabilityService {
    private final AvailabilityRepository availabilityRepository;

    @Autowired
    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    public Page<Availability> getAllAvailabilities(Pageable pageable) {
        return availabilityRepository.findAllBy(pageable);
    }

    public Availability getAvailabilityByID(int availabilityID) throws ResourceNotFoundException {
        return availabilityRepository.findByAvailabilityID(availabilityID)
                .orElseThrow(() -> new ResourceNotFoundException("Availability", "availabilityID", availabilityID));
    }

    public Availability addAvailability(Availability availability) throws ResourceAlreadyExistsException,
            InvalidTimeException {
        // Ensure no id conflict
        try {
            this.getAvailabilityByID(availability.getAvailabilityID());
            throw new ResourceAlreadyExistsException("Availability", "availabilityID", availability.getAvailabilityID());
        }
        catch (ResourceNotFoundException exception) {
            // Id does not exist yet, continue...
        }

        // Ensure start time comes before end time
        if (availability.getStartTime().isAfter(availability.getEndTime())) {
            throw new InvalidTimeException(availability.getStartTime(), availability.getEndTime());
        }

        availabilityRepository.save(availability);

        return availability;
    }

    public Availability rescheduleAvailability(Availability availability, int availabilityID) throws InvalidTimeException {
        Availability updatedAvailability = this.getAvailabilityByID(availabilityID);

        // Ensure times are still valid after update
        if (availability.getStartTime().isAfter(availability.getEndTime())) {
            throw new InvalidTimeException(availability.getStartTime(), availability.getEndTime());
        }

        updatedAvailability.setDayOfWeek(availability.getDayOfWeek());
        updatedAvailability.setStartTime(availability.getStartTime());
        updatedAvailability.setEndTime(availability.getEndTime());
        updatedAvailability.setDoctor(availability.getDoctor());

        availabilityRepository.save(updatedAvailability);

        return availability;

    }

    public Integer deleteAvailability(int availabilityID) {
        availabilityRepository.deleteById(availabilityID);

        return availabilityID;
    }
}
