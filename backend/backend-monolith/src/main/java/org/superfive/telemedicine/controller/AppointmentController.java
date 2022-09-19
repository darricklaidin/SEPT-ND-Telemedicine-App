package org.superfive.telemedicine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Get all appointments
    @GetMapping("")
    public ResponseEntity<Page<Appointment>> getAllAppointments(Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAllAppointments(pageable));
    }

    // Get an appointment by appointment ID
    @GetMapping("/{appointmentID}")
    public ResponseEntity<Appointment> getAppointmentByID(@PathVariable(value = "appointmentID") int appointmentID) {
        return ResponseEntity.ok(appointmentService.getAppointmentByID(appointmentID));
    }

    // Create a new appointment
    @PostMapping("")
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.addAppointment(appointment));
    }

    // Update (Reschedule) an existing appointment by ID
    @PutMapping("/{appointmentID}")
    public ResponseEntity<Appointment> rescheduleAppointment(@PathVariable(value = "appointmentID") int appointmentID,
                                                             @RequestBody Appointment updatedAppointment) {
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(appointmentID, updatedAppointment));
    }

    // Delete (Cancel) an existing appointment by ID
    @DeleteMapping("/{appointmentID}")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable(value = "appointmentID") int appointmentID) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentID));
    }
}
