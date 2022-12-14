package sept.superfive.prescriptionmicroservice.controller;

import sept.superfive.prescriptionmicroservice.model.Prescription;
import sept.superfive.prescriptionmicroservice.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sept.superfive.prescriptionmicroservice.payload.PrescriptionDTO;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    // Get all prescription
    @GetMapping("")
    public ResponseEntity<Page<Prescription>> getAllPrescriptions(Pageable pageable) {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions(pageable));
    }

    // Get prescription by ID
    @GetMapping("/{prescriptionID}")
    public ResponseEntity<Prescription> getPrescriptionByID(@PathVariable(value = "prescriptionID") int prescriptionID) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionByID(prescriptionID));
    }

    // Create prescriptions
    @PostMapping("")
    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
        return ResponseEntity.ok(prescriptionService.createPrescription(prescription));
    }

    // Get a doctor's prescriptions
    @GetMapping("/doctor/{doctorID}")
    public ResponseEntity<Page<Prescription>> getDoctorPrescriptions(
            @PathVariable(value = "doctorID") int doctorID,
            Pageable pageable
    ) {
        return ResponseEntity.ok(prescriptionService.getDoctorPrescriptions(doctorID, pageable));
    }

    // Get a patient's prescriptions
    @GetMapping("/patient/{patientID}")
    public ResponseEntity<Page<Prescription>> getPatientPrescriptions(
            @PathVariable(value = "patientID") int patientID,
            Pageable pageable
    ) {
        return ResponseEntity.ok(prescriptionService.getPatientPrescriptions(patientID, pageable));
    }

    // Update prescription
    @PutMapping("/{prescriptionID}")
    public ResponseEntity<Prescription> updatePrescription(
            @RequestBody PrescriptionDTO prescriptionDTO,
            @PathVariable(value = "prescriptionID") int prescriptionID
    ) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(prescriptionID, prescriptionDTO));
    }

    // Delete prescription
    @DeleteMapping("/{prescriptionID}")
    public ResponseEntity<Prescription> deletePrescription(@PathVariable(value = "prescriptionID") int prescriptionID) {
        return ResponseEntity.ok(prescriptionService.deletePrescription(prescriptionID));
    }
}
