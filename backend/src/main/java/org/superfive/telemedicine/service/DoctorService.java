package org.superfive.telemedicine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.superfive.telemedicine.exception.ResourceAlreadyExistsException;
import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Availability;
import org.superfive.telemedicine.model.Doctor;
import org.superfive.telemedicine.repository.DoctorRepository;
import org.superfive.telemedicine.utility.SortUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Get all doctors
    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAllBy(pageable);
    }

    // Get a doctor by ID
    public Doctor getDoctorByID(int doctorID) throws ResourceNotFoundException {
        return doctorRepository.findByUserID(doctorID)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "doctorID", doctorID));
    }

    // Get a doctor's appointments
    public List<Appointment> getDoctorAppointments(int doctorID, String sortMethod) {
        List<Appointment> appointments = new ArrayList<>(this.getDoctorByID(doctorID).getAppointments());
        SortUtility.sortAppointments(sortMethod, appointments);
        return appointments;
    }

    // Get a doctor's availabilities
    public List<Availability> getDoctorAvailabilities(int doctorID, String sortMethod) {
        List<Availability> availabilities = new ArrayList<>(this.getDoctorByID(doctorID).getAvailabilities());
        SortUtility.sortAvailabilities(sortMethod, availabilities);
        return availabilities;
    }

    // Create a new doctor
    public Doctor createDoctor(Doctor newDoctor) throws ResourceAlreadyExistsException{
        // Ensure doctor ID does not already exist
        try {
            this.getDoctorByID(newDoctor.getUserID());
            throw new ResourceAlreadyExistsException("Doctor", "doctorID", newDoctor.getUserID());
        }
        catch (ResourceNotFoundException exception) {
            // Doctor id does not exist, continue...
        }

        doctorRepository.save(newDoctor);

        return newDoctor;
    }

    // Update an existing doctor
    public Doctor updateDoctor(int doctorID, Doctor doctor) {
        // Ensure doctor exists
        Doctor updatedDoctor = this.getDoctorByID(doctorID);

        updatedDoctor.setFirstName(Objects.isNull(doctor.getFirstName()) ?
                updatedDoctor.getFirstName() : doctor.getFirstName());
        updatedDoctor.setLastName(Objects.isNull(doctor.getLastName()) ?
                updatedDoctor.getLastName() : doctor.getLastName());
        updatedDoctor.setEmail(Objects.isNull(doctor.getEmail()) ?
                updatedDoctor.getEmail() : doctor.getEmail());
        updatedDoctor.setPassword(Objects.isNull(doctor.getPassword()) ?
                updatedDoctor.getPassword() : doctor.getPassword());
        updatedDoctor.setSpecialty(Objects.isNull(doctor.getSpecialty()) ?
                updatedDoctor.getSpecialty() : doctor.getSpecialty());
        updatedDoctor.setAccountStatus(Objects.isNull(doctor.getAccountStatus()) ?
                updatedDoctor.getAccountStatus() : doctor.getAccountStatus());

        doctorRepository.save(updatedDoctor);

        return updatedDoctor;
    }
}
