package org.superfive.telemedicine.service;

import org.superfive.telemedicine.exception.ResourceNotFoundException;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.model.Doctor;
import org.superfive.telemedicine.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAllBy();
    }

    // Get a doctor by ID
    public Doctor getDoctorByID(int doctorID) throws ResourceNotFoundException {
        return doctorRepository.findByUserID(doctorID)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor ID", "doctorID", doctorID));
    }

    // Get a doctor's appointments
    public Set<Appointment> getDoctorAppointments(int doctorID) {
        return this.getDoctorByID(doctorID).getAppointments();
    }

    // Get all doctors by specialty
    public List<Doctor> getAllDoctorsBySpecialty(Integer specialtyID) {
        return doctorRepository.findAllBySpecialtySpecialtyID(specialtyID);
    }

    // Get all doctors by status
    public List<Doctor> getAllDoctorsByStatus(String accountStatus) {
        return doctorRepository.findAllByAccountStatus(accountStatus);
    }

    public List<Doctor> getAllDoctorsByFilter(Integer specialtyID, String accountStatus) {
        if (specialtyID == null && accountStatus == null) {
            return doctorRepository.findAllBy();
        }
        else if (specialtyID == null) {
            return doctorRepository.findAllByAccountStatus(accountStatus);
        }
        else if (accountStatus == null) {
            return doctorRepository.findAllBySpecialtySpecialtyID(specialtyID);
        }
        return doctorRepository.findAllBySpecialtySpecialtyIDAndAccountStatus(specialtyID, accountStatus);
    }
}
