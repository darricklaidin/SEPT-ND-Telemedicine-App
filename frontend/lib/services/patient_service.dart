

import '../models/appointment.dart';
import 'package:frontend/config/constants.dart';

import 'package:http/http.dart' as http;
import 'dart:convert';

import '../models/patient.dart';
import 'appointment_service.dart';
import 'auth_service.dart';

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

  static Future<List<Patient>> fetchPatientList() async {

    //initialize list of patients;
    List<Patient> patients = [];

    //fetching the data
    var response = await http.get(Uri.parse('$apiAuthRootUrl/patients'), headers: {
      'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJpZCI6IjEiLCJleHAiOjE2NjQ5Njk0NDQsImlhdCI6MTY2NDM2NDY0NCwiZW1haWwiOiJicnlhbkBnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6IlBBVElFTlQifQ.YK-cydutMzH9ohT58evJunY9FCUEnhguv-8_0sUSaT-Wm4HCusMIQT8XH6lpoOw_bNHk061npHPWYgGhM1bTPA'});

    if (response.statusCode == 200) {
      List<dynamic> jsonData = jsonDecode(response.body)["content"];  // list of patients

      for (dynamic patient in jsonData) {
        patients.add(Patient.fromJson(patient));
      }
      return patients;

    } else {
      throw Exception("Failed to load patient's list");
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

  static Future<List<Appointment>> fetchPatientAppointments(int patientID) async {
    var response = await http.get(Uri.parse(
        '$apiBookingRootUrl/appointments/patient/$patientID?sort=date&sort=startTime'))
        .timeout(const Duration(seconds: 5));

    if (response.statusCode == 200) {
      List<dynamic> jsonData = jsonDecode(response.body)['content'];  // list of appointments
      List<Appointment> appointments = [];
      for (dynamic appointment in jsonData) {
        Appointment? tempAppointment = await AppointmentService.getAppointmentFromJSON(appointment);
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


}
