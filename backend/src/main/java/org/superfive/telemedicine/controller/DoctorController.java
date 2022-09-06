package org.superfive.telemedicine.controller;

import org.springframework.web.bind.annotation.*;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Doctor;
import org.superfive.telemedicine.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Get doctors
    // Create doctors
    // Update doctors
    // DEACTIVATE doctors (possibly use POST to modify accountStatus value instead of DELETE)

    // Get all doctors by filter
    @GetMapping("")
    public ResponseEntity<List<Doctor>> getAllDoctorsByFilter(
            @RequestParam(value = "specialtyID", required = false) Integer specialtyID,
            @RequestParam(value = "accountStatus", required = false) String accountStatus
    ) {
        return ResponseEntity.ok(doctorService.getAllDoctorsByFilter(specialtyID, accountStatus));
    }

    // Get a doctor by ID
    @GetMapping("/{doctorID}")
    public ResponseEntity<Doctor> getDoctorByID(@PathVariable(value = "doctorID") int doctorID) {
        return ResponseEntity.ok(doctorService.getDoctorByID(doctorID));
    }

    // Get a doctor's appointments
    @GetMapping("/{doctorID}/appointments")
    public ResponseEntity<Set<Appointment>> getDoctorAppointments(@PathVariable(value = "doctorID") int doctorID) {
        return ResponseEntity.ok(doctorService.getDoctorAppointments(doctorID));
    }

}
