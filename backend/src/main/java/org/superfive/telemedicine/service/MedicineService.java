package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.exception.ResourceAlreadyExistsException;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Medicine;
import org.superfive.telemedicine.repository.MedicineRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Objects;
@Service
public class MedicineService {
    private final MedicineRepository medicineRepository;

    @Autowired
    public SpecialtyService(MedicineRepository specialityRepository) { this.medicineRepository = specialityRepository;}

    // Get all specialities
    public Page<Specialty> getAllSpecialties(Pageable pageable) { return medicineRepository.findAllBy(pageable);}

    public Specialty getSpecialtyByID(int specialtyID) throws ResourceNotFoundException {
        return medicineRepository.findBySpecialtyID(specialtyID)
                .orElseThrow(() -> new ResourceNotFoundException("Specialty", "specialtyID", specialtyID));
    }

    public Specialty createSpecialty(Specialty newSpecialty) throws ResourceAlreadyExistsException {
        // Ensure specialty ID does not already exist
        try {
            this.getSpecialtyByID(newSpecialty.getSpecialtyID());
            throw new ResourceAlreadyExistsException("Specialty", "specialtyID", newSpecialty.getSpecialtyID());
        } catch (ResourceNotFoundException exception) {
            // Specialty id does not exist, continue...
        }

        medicineRepository.save(newSpecialty);

        return newSpecialty; // for the JSON - printed out.
    }

    public Specialty updateSpecialty(int specialtyID, Specialty specialty) {
        Specialty updatedSpecialty = this.getSpecialtyByID(specialtyID); //Also checks if specialtyID exists

        updatedSpecialty.setSpecialtyName(
                Objects.isNull(specialty.getSpecialtyName()) ?
                        updatedSpecialty.getSpecialtyName() : specialty.getSpecialtyName()
        );
        // TERNARY OPERATOR -- Three parameters// Shortened form of IF-ELSE Statement.
        // IF Objects.isNull(...) is true Then what comes after ? is true and everything after : is false (else).

        medicineRepository.save(updatedSpecialty);


        return updatedSpecialty;

        //DO NOT WANT MANY-TO-ONE to be the JSON IGNORE.

    }

    public Specialty deleteSpecialty(int specialtyID) {
        Specialty deletedSpecialty = this.getSpecialtyByID(specialtyID);
        medicineRepository.deleteById(specialtyID);
        return deletedSpecialty;
    }

}