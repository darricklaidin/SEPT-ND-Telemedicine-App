import 'dart:async';

import '../models/appointment.dart';
import 'package:frontend/config/constants.dart';

import 'package:http/http.dart' as http;
import 'dart:convert';

import '../models/patient.dart';
import 'auth_service.dart';
import 'doctor_service.dart';

class PatientService {

  static Future<List<Patient>> fetchAllPatients() async {
    final response = await http.get(Uri.parse('$apiAuthRootUrl/patients?sort=firstName'),
        headers: {
          'Authorization': 'Bearer ${await getJWT()}',
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
      'Authorization': 'Bearer ${await getJWT()}',
    });

    if (response.statusCode == 200) {
      return Patient.fromJson(jsonDecode(response.body));
    } else if (response.statusCode == 404) {
      return jsonDecode(response.body)['message'];
    } else {
      throw Exception('Failed to load patient profile');
    }
  }

  static Future<List<Appointment>> fetchPatientAppointments() async {
    int patientID = await getUserIdFromStorage();

    var response = await http.get(Uri.parse(
        '$apiBookingRootUrl/appointments/patient/$patientID?sort=date&sort=startTime'))
        .timeout(const Duration(seconds: 5));

    if (response.statusCode == 200) {
      List<dynamic> jsonData = jsonDecode(response.body)['content'];  // list of appointments
      List<Appointment> appointments = [];
      for (dynamic appointment in jsonData) {
        appointments.add(await DoctorService.getAppointmentFromJSON(appointment));
      }
      return appointments;
    } else {
      throw Exception("Failed to load patient's appointments");
    }
  }


}
