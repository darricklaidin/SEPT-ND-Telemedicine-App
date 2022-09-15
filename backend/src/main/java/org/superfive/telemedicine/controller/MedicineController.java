package org.superfive.telemedicine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.superfive.telemedicine.model.Medicine;
import org.superfive.telemedicine.service.MedicineService;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    @Autowired
    public MedicineController(MedicineService medicineService) {this.medicineService = medicineService;}

    // Get all specialties
    @GetMapping("")
    public ResponseEntity<Page<Medicine>> getAllMedicines(Pageable pageable) {
        return ResponseEntity.ok(medicineService.getAllMedicines(pageable));
    }

    // Get specialty by ID
    @GetMapping("/{medicineID}")
    public ResponseEntity<Medicine> getMedicineByID(@PathVariable(value = "medicineID") int medicineID) {
        return ResponseEntity.ok(medicineService.getMedicineByID(medicineID));
    }

    // Create specialties
    @PostMapping("")
    public ResponseEntity<Medicine> createMedicine(@RequestBody Medicine medicine) {
        return ResponseEntity.ok(medicineService.createMedicine(medicine));
    }

    // Update specialties
    @PutMapping("/{medicineID}")
    public ResponseEntity<Medicine> updateMedicine(
            @RequestBody Medicine medicine,
            @PathVariable(value = "medicineID") int medicineID
    ) {
        return ResponseEntity.ok(medicineService.updateMedicine(medicineID, medicine));
    }

    // Delete specialties
    @DeleteMapping("/medicineID}")
    public ResponseEntity<Medicine> deleteMedicine(@PathVariable(value = "medicineID") int medicineID) {
        return ResponseEntity.ok(medicineService.deleteMedicine(medicineID));
    }

}
