package org.superfive.telemedicine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.superfive.telemedicine.model.Appointment;
import org.superfive.telemedicine.service.AppointmentService;

import java.util.Map;
import java.util.Set;

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
    public ResponseEntity<Set<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    // Get an appointment by ID
    @GetMapping("/{appointmentID}")
    public ResponseEntity<Appointment> getAppointmentByID(@PathVariable(value = "appointmentID") int appointmentID) {
        return ResponseEntity.ok(appointmentService.getAppointmentByID(appointmentID));
    }

    // TODO: Maybe combine date and time -> schedule; think about how one would access each endpoint
    // Get appointments by date
//        // Date can be defined as <YYYYMMDD> in the URI (e.g. ../api/appointments/d10062002 - meaning 10 June 2002)
//    @GetMapping("/{date}")
//    public ResponseEntity<Appointment> getAppointmentsByDate(@PathVariable(value = "date") String date) {
//        return ResponseEntity.ok(appointmentService.getAppointmentsByDate(date));
//    }
//
    // Get an appointment by time
        // Time can be defined as <HHMMSS> 24 hour format in the URI (e.g. ../api/appointments/t223015 - meaning 22:30:15)
//    @GetMapping("/{time}")
//    public ResponseEntity<Appointment> getAppointmentsByTime(@PathVariable(value = "time") String time) {
//        return ResponseEntity.ok(appointmentService.getAppointmentsByTime(time));
//    }

    // Get all appointments by status (i.e. get all upcoming appointments)
    @GetMapping("/status/{appointmentStatus}")
    public ResponseEntity<Set<Appointment>> getAllAppointmentsByStatus(
            @PathVariable(value = "appointmentStatus") String appointmentStatus
    ) {
        return ResponseEntity.ok(appointmentService.getAllAppointmentsByStatus(appointmentStatus));
    }

    // Get an appointment's status
    @GetMapping("/{appointmentID}/status")
    public ResponseEntity<String> getAppointmentStatus(@PathVariable(value = "appointmentID") int appointmentID) {
        return ResponseEntity.ok(appointmentService.getAppointmentStatus(appointmentID));
    }

    // Get appointment participants
    @GetMapping("/{appointmentID}/participants")
    public ResponseEntity<Map<String, Integer>> getAppointmentParticipants(
            @PathVariable(value = "appointmentID") int appointmentID
    ) {
        return ResponseEntity.ok(appointmentService.getAppointmentParticipants(appointmentID));
    }

//    // Add a new appointment
//    @PostMapping("")
//    public Appointment addAppointment(@RequestBody Appointment appointment) {
//        return appointmentService.addAppointment(appointment);
//    }
//
//
//    // Update (Reschedule) an existing appointment by ID
//    @PutMapping("/{appointmentID}")
//    public Appointment rescheduleAppointmentByID(@PathVariable(value = "appointmentID") int appointmentID,
//                                                 @RequestBody Appointment appointment) {
//        return appointmentService.rescheduleAppointmentByID(appointmentID, appointment);
//    }
//
//
//    // Delete (Cancel) an existing appointment by ID
//    @DeleteMapping("/{appointmentID}")
//    public Appointment cancelAppointmentByID(@PathVariable(value = "appointmentID") int appointmentID) {
//        return appointmentService.cancelAppointmentByID(appointmentID);
//    }
}
