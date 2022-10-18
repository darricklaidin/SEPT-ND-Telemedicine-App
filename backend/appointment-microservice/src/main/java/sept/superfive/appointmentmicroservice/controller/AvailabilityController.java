package sept.superfive.appointmentmicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sept.superfive.appointmentmicroservice.dto.AvailabilityDTO;
import sept.superfive.appointmentmicroservice.model.Availability;
import sept.superfive.appointmentmicroservice.service.AvailabilityService;


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
    @GetMapping("/doctor/{doctorID}")
    public ResponseEntity<Page<Availability>> getDoctorAvailabilities(
            @PathVariable(value = "doctorID") int doctorID,
            Pageable pageable
    ) {
        return ResponseEntity.ok(availabilityService.getDoctorAvailabilities(doctorID, pageable));
    }

    // Add availability
    @PostMapping("")
    public ResponseEntity<Availability> addAvailability(@RequestBody AvailabilityDTO availability) {
        return ResponseEntity.ok(availabilityService.addAvailability(availability));
    }

    // Update availability
    @PutMapping("/{availabilityID}")
    public ResponseEntity<Availability> rescheduleAvailability(
            @RequestBody AvailabilityDTO availability,
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
