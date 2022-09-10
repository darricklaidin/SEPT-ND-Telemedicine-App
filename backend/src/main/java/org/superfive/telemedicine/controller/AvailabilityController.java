package org.superfive.telemedicine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.superfive.telemedicine.model.Availability;
import org.superfive.telemedicine.service.AvailabilityService;

@RestController
@RequestMapping("/api/availabilities")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    @Autowired
    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // TODO: Get all availabilities by filter
    @GetMapping("")
    public ResponseEntity<Page<Availability>> getAllAvailabilitiesByFilter(Pageable pageable) {
        return ResponseEntity.ok(availabilityService.getAllAvailabilitiesByFilter(pageable));
    }

    // Get availability by id
    @GetMapping("/{availabilityID}")
    public ResponseEntity<Availability> getAvailabilityByID(@PathVariable(value = "availabilityID") int availabilityID) {
        return ResponseEntity.ok(availabilityService.getAvailabilityByID(availabilityID));
    }

    // Add availability
    @PostMapping("")
    public ResponseEntity<Availability> addAvailability(@RequestBody Availability availability) {
        return ResponseEntity.ok(availabilityService.addAvailability(availability));
    }

    // Update availability
    @PutMapping("/{availabilityID}")
    public ResponseEntity<Availability> rescheduleAvailability(
            @RequestBody Availability availability,
            @PathVariable(value = "availabilityID") int availabilityID
    ) {
        return ResponseEntity.ok(availabilityService.rescheduleAvailability(availability, availabilityID));
    }

    // Delete availability
    @DeleteMapping("/{availabilityID}")
    public ResponseEntity<Integer> deleteAvailability(@PathVariable(value = "availabilityID") int availabilityID) {
        return ResponseEntity.ok(availabilityService.deleteAvailability(availabilityID));
    }
}
