package org.superfive.telemedicine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Patient;
import org.superfive.telemedicine.service.PatientService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Get all patients by filter
    @GetMapping("")
    public ResponseEntity<List<Patient>> getAllPatientsByFilter(
            @RequestParam(value = "accountStatus", required = false) String accountStatus
    ) {
        return ResponseEntity.ok(patientService.getAllPatientsByFilter(accountStatus));
    }

    // Get a patient by ID
    @GetMapping("/{patientID}")
    public ResponseEntity<Patient> getPatientByID(@PathVariable(value = "patientID") int patientID) {
        return ResponseEntity.ok(patientService.getPatientByID(patientID));
    }

    // Get a patient's appointments
    @GetMapping("/{patientID}/appointments")
    public ResponseEntity<Set<Appointment>> getPatientAppointments(@PathVariable(value = "patientID") int patientID) {
        return ResponseEntity.ok(patientService.getPatientAppointments(patientID));
    }




    // Create patients
    // Update patient
    // Deactivate patient (possibly use POST to modify accountStatus value instead of DELETE)
}
