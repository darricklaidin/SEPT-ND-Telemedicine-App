package sept.superfive.appointmentmicroservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sept.superfive.appointmentmicroservice.model.Appointment;
import sept.superfive.appointmentmicroservice.service.AppointmentService;


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

    // Get a doctor's appointments
    @GetMapping("/doctor/{doctorID}")
    public ResponseEntity<Page<Appointment>> getDoctorAppointments(
            @PathVariable(value = "doctorID") int doctorID,
            Pageable pageable
    ) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(doctorID, pageable));
    }

    // Get a patient's appointments
    @GetMapping("/patient/{patientID}")
    public ResponseEntity<Page<Appointment>> getPatientAppointments(
            @PathVariable(value = "patientID") int patientID,
            Pageable pageable
    ) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(patientID, pageable));
    }

    // Create a new appointment
    @PostMapping("")
    public ResponseEntity<Appointment> addAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.addAppointment(appointment));
    }

    // Delete (Cancel) an existing appointment by ID
    @DeleteMapping("/{appointmentID}")
    public ResponseEntity<Appointment> cancelAppointment(@PathVariable(value = "appointmentID") int appointmentID) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(appointmentID));
    }
}
