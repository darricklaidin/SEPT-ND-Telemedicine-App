package com.sept.authmicroservice.service;

import com.sept.authmicroservice.exception.ResourceAlreadyExistsException;
import com.sept.authmicroservice.exception.ResourceNotFoundException;
import com.sept.authmicroservice.model.Specialty;
import com.sept.authmicroservice.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sept.authmicroservice.payload.SpecialtyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class SpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private static final String RESOURCE_NAME = "Specialty";

    @Autowired
    public SpecialtyService(SpecialtyRepository specialityRepository) {
        this.specialtyRepository = specialityRepository;
    }

    // Get all specialities
    public Page<Specialty> getAllSpecialties(Pageable pageable) {
        return specialtyRepository.findAllBy(pageable);
    }

    public Specialty getSpecialtyByID(int specialtyID) throws ResourceNotFoundException {
        return specialtyRepository.findBySpecialtyID(specialtyID)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "specialtyID", specialtyID));
    }

    public Specialty getSpecialtyByName(String name) throws ResourceNotFoundException {
        return specialtyRepository.findBySpecialtyName(name)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "specialtyName", name));
    }

    public Specialty createSpecialty(SpecialtyDTO newSpecialty) throws ResourceAlreadyExistsException {
        // Ensure specialty name does not already exist
        try {
            this.getSpecialtyByName(newSpecialty.getSpecialtyName());
            throw new ResourceAlreadyExistsException(RESOURCE_NAME, "specialtyName", newSpecialty.getSpecialtyName());
        } catch (ResourceNotFoundException exception) {
            // Specialty name does not exist, continue...
        }
        Specialty temp = new Specialty(newSpecialty.getSpecialtyName());
        specialtyRepository.save(temp);
        return temp;
    }

    public Specialty updateSpecialty(int specialtyID, SpecialtyDTO specialtyDTO) {
        Specialty updatedSpecialty = this.getSpecialtyByID(specialtyID); //Also checks if specialtyID exists
        updatedSpecialty.setSpecialtyName(specialtyDTO.getSpecialtyName());

        return specialtyRepository.save(updatedSpecialty);
    }

    public Specialty deleteSpecialty(int specialtyID) {
        Specialty deletedSpecialty = this.getSpecialtyByID(specialtyID);
        specialtyRepository.deleteById(specialtyID);
        return deletedSpecialty;
    }
}
