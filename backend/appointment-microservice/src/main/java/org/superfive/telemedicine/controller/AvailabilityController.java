package org.superfive.telemedicine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.superfive.telemedicine.model.Availability;
import org.superfive.telemedicine.service.AvailabilityService;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    @Autowired
    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    // Get all availabilities
    @GetMapping("")
    public ResponseEntity<Page<Availability>> getAllAvailabilities(Pageable pageable) {
        return ResponseEntity.ok(availabilityService.getAllAvailabilities(pageable));
    }

    // Get availability by id
    @GetMapping("/{availabilityID}")
    public ResponseEntity<Availability> getAvailabilityByID(@PathVariable(value = "availabilityID") int availabilityID) {
        return ResponseEntity.ok(availabilityService.getAvailabilityByID(availabilityID));
    }

    // Get a doctor's availabilities
    @GetMapping("/{doctorID}/availabilities")
    public ResponseEntity<List<Availability>> getDoctorAvailabilities(
            @PathVariable(value = "doctorID") int doctorID,
            @RequestParam(value = "sort", required = false) String sortMethod
    ) {
        return ResponseEntity.ok(availabilityService.getDoctorAvailabilities(doctorID, sortMethod));
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
    public ResponseEntity<Availability> deleteAvailability(@PathVariable(value = "availabilityID") int availabilityID) {
        return ResponseEntity.ok(availabilityService.deleteAvailability(availabilityID));
    }
}
