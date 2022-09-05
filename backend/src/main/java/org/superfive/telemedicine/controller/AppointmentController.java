package org.superfive.telemedicine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.service.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // GET Requests ---------------------------------------------------------------------------------------------------

    // Get all appointments
    @GetMapping("")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    // Get an appointment by ID
    @GetMapping("/{appointmentID}")
    public ResponseEntity<Appointment> getAppointmentByID(@PathVariable(value = "appointmentID") int appointmentID) {
        return ResponseEntity.ok(appointmentService.getAppointmentByID(appointmentID));
    }

//    // Get appointments by date
//        // Date can be defined as <YYYYMMDD> in the URI (e.g. ../api/appointments/d10062002 - meaning 10 June 2002)
//    @GetMapping("/{date}")
//    public ResponseEntity<Appointment> getAppointmentsByDate(@PathVariable(value = "date") String date) {
//        return ResponseEntity.ok(appointmentService.getAppointmentsByDate(date));
//    }
//
//    // Get an appointment by time
//        // Time can be defined as <HHMMSS> 24 hour format in the URI (e.g. ../api/appointments/t223015 - meaning 22:30:15)
//    @GetMapping("/{time}")
//    public ResponseEntity<Appointment> getAppointmentsByTime(@PathVariable(value = "time") String time) {
//        return ResponseEntity.ok(appointmentService.getAppointmentsByTime(time));
//    }

    // Get appointments where a specific doctor is a participant
    @GetMapping("/doctor/{doctorID}")
    public List<Appointment> getAppointmentsByDoctorID(@PathVariable(value = "doctorID") int doctorID) {
        return appointmentService.getAppointmentsByDoctorID(doctorID);
    }

//    // Get appointments where a specific patient is a participant
//    @GetMapping("/{patientID}")
//    public ResponseEntity<Appointment> getAppointmentsByPatientID(@PathVariable(value = "patientID") int patientID) {
//        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientID(patientID));
//    }
//
//    // POST Requests ---------------------------------------------------------------------------------------------------
//
//    // Add a new appointment
//    @PostMapping("")
//    public Appointment addAppointment(@RequestBody Appointment appointment) {
//        return appointmentService.addAppointment(appointment);
//    }
//
//    // PUT (UPDATE) Requests -------------------------------------------------------------------------------------------
//
//    // Update (Reschedule) an existing appointment by ID
//    @PutMapping("/{appointmentID}")
//    public Appointment rescheduleAppointmentByID(@PathVariable(value = "appointmentID") int appointmentID,
//                                                 @RequestBody Appointment appointment) {
//        return appointmentService.rescheduleAppointmentByID(appointmentID, appointment);
//    }
//
//    // DELETE Requests -------------------------------------------------------------------------------------------------
//
//    // Delete (Cancel) an existing appointment by ID
//    @DeleteMapping("/{appointmentID}")
//    public Appointment cancelAppointmentByID(@PathVariable(value = "appointmentID") int appointmentID) {
//        return appointmentService.cancelAppointmentByID(appointmentID);
//    }
}
