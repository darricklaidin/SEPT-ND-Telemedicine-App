package org.superfive.telemedicine.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Availability;
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

    // Create doctors
    // Update doctors
    // DEACTIVATE doctors (possibly use POST to modify accountStatus value instead of DELETE)

    // TODO: Get all doctors by filter
    @GetMapping("")
    public ResponseEntity<Page<Doctor>> getAllDoctorsByFilter(Pageable pageable) {
        return ResponseEntity.ok(doctorService.getAllDoctorsByFilter(pageable));
    }

    // Get a doctor by ID
    @GetMapping("/{doctorID}")
    public ResponseEntity<Doctor> getDoctorByID(@PathVariable(value = "doctorID") int doctorID) {
        return ResponseEntity.ok(doctorService.getDoctorByID(doctorID));
    }

    // Get a doctor's appointments
    @GetMapping("/{doctorID}/appointments")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(
            @PathVariable(value = "doctorID") int doctorID,
            @RequestParam(value = "sort", required = false) String sortMethod
    ) {
        return ResponseEntity.ok(doctorService.getDoctorAppointments(doctorID, sortMethod));
    }

    // Get a doctor's availabilities
    @GetMapping("/{doctorID}/availabilities")
    public ResponseEntity<List<Availability>> getDoctorAvailabilities(
            @PathVariable(value = "doctorID") int doctorID,
            @RequestParam(value = "sort", required = false) String sortMethod
    ) {
        return ResponseEntity.ok(doctorService.getDoctorAvailabilities(doctorID, sortMethod));
    }


}