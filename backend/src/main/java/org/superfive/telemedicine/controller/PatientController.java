package org.superfive.telemedicine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Patient;
import org.superfive.telemedicine.service.PatientService;

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

    // Get a patient's appointments
    // Sort by appointmentID, appointmentSchedule
    @GetMapping("/{patientID}/appointments")
    public ResponseEntity<List<Appointment>> getPatientAppointments(
            @PathVariable(value = "patientID") int patientID,
            @RequestParam(value = "sort", required = false) String sortMethod
    ) {
        return ResponseEntity.ok(patientService.getPatientAppointments(patientID, sortMethod));
    }

    // Create a new patient
    @PostMapping("")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.createPatient(patient));
    }

    // Update patient
    // Deactivate patient (use PUT to modify accountStatus value instead of DELETE)
    @PutMapping("/{patientID}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable(value = "patientID") int patientID,
            @RequestBody Patient patient
    ) {
        return ResponseEntity.ok(patientService.updatePatient(patientID, patient));
    }

    // Delete patient by id
    @DeleteMapping("/{patientID}")
    public ResponseEntity<Patient> deletePatient(@PathVariable(value = "patientID") int patientID) {
        return ResponseEntity.ok(patientService.deletePatient(patientID));
    }

}