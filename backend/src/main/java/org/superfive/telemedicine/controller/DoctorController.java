package org.superfive.telemedicine.controller;

import org.superfive.telemedicine.model.Doctor;
import org.superfive.telemedicine.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


    // Get all doctors
    @GetMapping("")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // Get a doctor by ID
    @GetMapping("/{doctorID}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable(value = "doctorID") int doctorID) {
        return ResponseEntity.ok(doctorService.getDoctorById(doctorID));
    }
}
