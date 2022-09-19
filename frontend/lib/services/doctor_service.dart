import '../models/appointment.dart';
import 'package:frontend/config/constants.dart';

import 'package:http/http.dart' as http;
import 'dart:convert';

class PatientService {

  static Future<List<Appointment>> fetchDoctorAppointments(int doctorID) async {
    var response = await http.get(Uri.parse('$apiRootUrl/doctors/$doctorID/appointments?sort=datetime'));

    if (response.statusCode == 200) {
      var jsonData = jsonDecode(response.body);
      return jsonData.map<Appointment>((appointment) => Appointment.fromJson(appointment)).toList();
    }
    else {
      throw Exception("Failed to load doctor's appointments");
    }
  }

}