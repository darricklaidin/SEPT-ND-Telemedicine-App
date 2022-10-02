package com.sept.authmicroservice.controller;

import com.sept.authmicroservice.model.Doctor;
import com.sept.authmicroservice.model.User;
import com.sept.authmicroservice.payload.DoctorSignUp;
import com.sept.authmicroservice.service.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Get all doctors
    @GetMapping("")
    public ResponseEntity<Page<Doctor>> getAllDoctors(Pageable pageable) {
        return ResponseEntity.ok(doctorService.getAllDoctors(pageable));
    }

    // Get a doctor by ID
    @GetMapping("/{doctorID}")
    public ResponseEntity<Doctor> getDoctorByID(@PathVariable(value = "doctorID") int doctorID) {
        return ResponseEntity.ok(doctorService.getDoctorByID(doctorID));
    }

    // Update doctors
    // Deactivate doctors (use PUT to modify accountStatus value instead of DELETE)
    @PutMapping("/{doctorID}")
    public ResponseEntity<Doctor> updateDoctor(
            @RequestBody Doctor doctor,
            @PathVariable(value = "doctorID") int doctorID
    ) {
        return ResponseEntity.ok(doctorService.updateDoctor(doctorID, doctor));
    }

    // Delete doctors
    @DeleteMapping("/{doctorID}")
    public ResponseEntity<Doctor> deleteDoctor(@PathVariable(value = "doctorID") int doctorID) {
        return ResponseEntity.ok(doctorService.deleteDoctor(doctorID));
    }

}