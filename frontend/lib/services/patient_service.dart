import 'dart:async';

import '../models/appointment.dart';
import 'package:frontend/config/constants.dart';

import 'package:http/http.dart' as http;
import 'dart:convert';

import '../models/patient.dart';
import 'appointment_service.dart';
import 'auth_service.dart';

class PatientService {
  static Future<List<Patient>> fetchAllPatients() async {
    final response = await http
        .get(Uri.parse('$apiAuthRootUrl/patients?sort=firstName'), headers: {
      'Authorization': 'Bearer ${await AuthService.getJWT()}',
    }).timeout(const Duration(seconds: 5));

    if (response.statusCode == 200) {
      return jsonDecode(response.body)['content']
          .map<Patient>((patient) => Patient.fromJson(patient))
          .toList();
    } else {
      throw Exception('Failed to load patients');
    }
  }

  static Future fetchPatient(int patientID) async {
    final response = await http
        .get(Uri.parse('$apiAuthRootUrl/patients/$patientID'), headers: {
      'Authorization': 'Bearer ${await AuthService.getJWT()}',
    });

    if (response.statusCode == 200) {
      return Patient.fromJson(jsonDecode(response.body));
    } else if (response.statusCode == 404) {
      return jsonDecode(response.body)['message'];
    } else {
      throw Exception('Failed to load patient profile');
    }
  }

  static Future<List<Appointment>> fetchPatientAppointments(
      int patientID) async {
    var response = await http
        .get(Uri.parse(
            '$apiBookingRootUrl/appointments/patient/$patientID?sort=date&sort=startTime'))
        .timeout(const Duration(seconds: 5));

    if (response.statusCode == 200) {
      List<dynamic> jsonData =
          jsonDecode(response.body)['content']; // list of appointments
      List<Appointment> appointments = [];
      for (dynamic appointment in jsonData) {
        Appointment? tempAppointment =
            await AppointmentService.getAppointmentFromJSON(appointment);
        if (tempAppointment != null) {
          appointments.add(tempAppointment);
        } else {
          // TODO: Appointment should be deleted from database
        }
      }
      return appointments;
    } else {
      throw Exception("Failed to load patient's appointments");
    }
  }

  /// Update the patient with the specified [patientID].
  /// Returns the updated [patient] if successful, otherwise returns the error message.
  /// Leave [newPassword] as [null] if you don't want to change the password.
  static Future updatePatient(
      int patientID, Patient patient, String? newPassword) async {
    // if new password is null, use old password
    var response =
        await http.put(Uri.parse('$apiAuthRootUrl/patients/$patientID'),
            headers: {
              'Authorization': 'Bearer ${await AuthService.getJWT()}',
              'Content-Type': 'application/json',
            },
            body: jsonEncode(patient.toJson(newPassword)));

    Map<String, dynamic> decodedResponse = jsonDecode(response.body);

    if (response.statusCode == 200) {
      return "Success";
    } else if (decodedResponse['message'] == "Resource Already Exists") {
      throw Exception("A user with that email already exists");
    } else {
      throw Exception('Failed to update patient profile');
    }
  }
}
