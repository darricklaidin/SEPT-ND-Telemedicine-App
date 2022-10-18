package sept.superfive.authmicroservice.controller;

import sept.superfive.authmicroservice.model.Doctor;
import sept.superfive.authmicroservice.service.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

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