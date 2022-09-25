import '../models/appointment.dart';
import 'package:frontend/config/constants.dart';

import 'package:http/http.dart' as http;
import 'dart:convert';

import '../models/patient.dart';
import 'auth_service.dart';
import 'doctor_service.dart';

class PatientService {
  static Future<Patient> fetchPatient(int patientID) async {
    final response = await http
        .get(Uri.parse('$apiAuthRootUrl/patients/$patientID'), headers: {
      'Authorization': 'Bearer ${await getJWT()}',
    });

    if (response.statusCode == 200) {
      return Patient.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to load patient profile');
    }
  }

  static Future<List<Appointment>> fetchPatientAppointments() async {
    int patientID = await getUserIdFromStorage();

    var response = await http.get(Uri.parse(
        '$apiBookingRootUrl/appointments/patient/$patientID?sort=date&sort=startTime'));

    if (response.statusCode == 200) {
      List<dynamic> jsonData = jsonDecode(response.body)['content'];  // list of appointments
      List<Appointment> appointments = [];
      for (dynamic appointment in jsonData) {
        appointments.add(await DoctorService.getAppointmentFromJSON(appointment));
      }
      return appointments;
    } else {
      // TODO: display error message
      throw Exception("Failed to load patient's appointments");
    }
  }
}
