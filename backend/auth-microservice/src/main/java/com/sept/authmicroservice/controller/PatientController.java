package com.sept.authmicroservice.controller;

import com.sept.authmicroservice.model.Patient;
import com.sept.authmicroservice.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Get all patients
    @GetMapping("")
    public ResponseEntity<Page<Patient>> getAllPatients(Pageable pageable) {
        return ResponseEntity.ok(patientService.getAllPatients(pageable));
    }

    // Get a patient by ID
    @GetMapping("/{patientID}")
    public ResponseEntity<Patient> getPatientByID(@PathVariable(value = "patientID") int patientID) {
        return ResponseEntity.ok(patientService.getPatientByID(patientID));
    }

    // Delete patient by id
    @DeleteMapping("/{patientID}")
    public ResponseEntity<Patient> deletePatient(@PathVariable(value = "patientID") int patientID) {
        return ResponseEntity.ok(patientService.deletePatient(patientID));
    }
}
