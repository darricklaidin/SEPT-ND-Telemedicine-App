import '../models/appointment.dart';
import 'package:frontend/config/constants.dart';

import 'package:http/http.dart' as http;
import 'dart:convert';

class PatientService {

  static Future<List<Appointment>> fetchPatientAppointments() async {
    int patientID = 1;  // TODO: get patientID from login

    var response = await http.get(Uri.parse('$apiRootUrl/patients/$patientID/appointments?sort=datetime'));
    if (response.statusCode == 200) {
      var jsonData = jsonDecode(response.body);
      return jsonData.map<Appointment>((appointment) => Appointment.fromJson(appointment)).toList();
    }
    else {
      throw Exception('Failed to load patients');
    }
  }

}