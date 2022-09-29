import 'dart:convert';
import 'package:http/http.dart' as http;

import 'package:frontend/config/constants.dart';
import 'package:frontend/services/doctor_service.dart';
import 'package:frontend/services/patient_service.dart';
import '../models/appointment.dart';
import '../models/doctor.dart';
import '../models/patient.dart';

class AppointmentService {

  static Future deleteAppointment(int appointmentID) async {
    var response = await http
        .delete(Uri.parse('$apiBookingRootUrl/appointments/$appointmentID'));
    if (response.statusCode == 200) {
      print("Deleted appointment $appointmentID");
    } else {
      throw Exception('Failed to delete appointment $appointmentID');
    }
  }

  static Future findAppointmentByID(int appointmentID) async {
    var response = await http
        .get(Uri.parse('$apiBookingRootUrl/appointments/$appointmentID'));
    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else if (response.statusCode == 404) {
      return jsonDecode(response.body)['message'];
    } else {
      throw Exception('Failed to find appointment $appointmentID');
    }
  }

  static Future getAppointmentFromJSON(appointment) async {
    dynamic tempDoctor = await DoctorService.fetchDoctor(appointment['doctorID']);

    if (tempDoctor == "Resource Not Found") {
      return null;
    }

    Doctor doctor = tempDoctor;

    dynamic tempPatient = await PatientService.fetchPatient(appointment['patientID']);

    if (tempPatient == "Resource Not Found") {
      return null;
    }

    Patient patient = tempPatient;

    return Appointment.fromJson(appointment, doctor, patient);
  }

}
