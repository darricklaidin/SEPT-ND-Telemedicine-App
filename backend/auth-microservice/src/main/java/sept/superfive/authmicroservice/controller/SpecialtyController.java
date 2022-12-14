package sept.superfive.authmicroservice.controller;

import sept.superfive.authmicroservice.model.Specialty;
import sept.superfive.authmicroservice.service.SpecialtyService;
import sept.superfive.authmicroservice.payload.SpecialtyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {
    private final SpecialtyService specialtyService;

    @Autowired
    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    // Get all specialties
    @GetMapping("")
    public ResponseEntity<Page<Specialty>> getAllSpecialties(Pageable pageable) {
        return ResponseEntity.ok(specialtyService.getAllSpecialties(pageable));
    }

    // Get specialty by ID
    @GetMapping("/{specialtyID}")
    public ResponseEntity<Specialty> getSpecialtyByID(@PathVariable(value = "specialtyID") int specialtyID) {
        return ResponseEntity.ok(specialtyService.getSpecialtyByID(specialtyID));
    }

    // Create specialties
    @PostMapping("")
    public ResponseEntity<Specialty> createSpecialty(@RequestBody SpecialtyDTO specialtyDTO) {
        return ResponseEntity.ok(specialtyService.createSpecialty(specialtyDTO));
    }

    // Update specialties
    @PutMapping("/{specialtyID}")
    public ResponseEntity<Specialty> updateSpecialty(
            @RequestBody SpecialtyDTO specialtyDTO,
            @PathVariable(value = "specialtyID") int specialtyID
    ) {
        return ResponseEntity.ok(specialtyService.updateSpecialty(specialtyID, specialtyDTO));
    }

    // Delete specialties
    @DeleteMapping("/{specialtyID}")
    public ResponseEntity<Specialty> deleteSpecialty(@PathVariable(value = "specialtyID") int specialtyID) {
        return ResponseEntity.ok(specialtyService.deleteSpecialty(specialtyID));
    }
}
