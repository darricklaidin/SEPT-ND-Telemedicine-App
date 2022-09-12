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

    // TODO: Get all appointments by filter (CRITERIA API)
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
    public ResponseEntity<Appointment> rescheduleAppointmentByID(@PathVariable(value = "appointmentID") int appointmentID,
                                                                 @RequestBody Appointment updatedAppointment) {
        return ResponseEntity.ok(appointmentService.rescheduleAppointmentByID(appointmentID, updatedAppointment));
    }

    // Delete (Cancel) an existing appointment by ID
    @DeleteMapping("/{appointmentID}")
    public ResponseEntity<Integer> cancelAppointmentByID(@PathVariable(value = "appointmentID") int appointmentID) {
        return ResponseEntity.ok(appointmentService.cancelAppointmentByID(appointmentID));
    }
}
