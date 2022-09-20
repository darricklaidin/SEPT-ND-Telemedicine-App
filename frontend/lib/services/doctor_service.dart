import 'package:frontend/models/availability.dart';

import '../models/appointment.dart';
import 'package:frontend/config/constants.dart';

import 'package:http/http.dart' as http;
import 'dart:convert';

class DoctorService {

  static Future<List<Appointment>> fetchDoctorAppointments(int doctorID) async {
    var response = await http.get(Uri.parse('$apiAuthRootUrl/doctors/$doctorID/appointments?sort=datetime'));

    if (response.statusCode == 200) {
      var jsonData = jsonDecode(response.body);
      return jsonData.map<Appointment>((appointment) => Appointment.fromJson(appointment)).toList();
    }
    else {
      throw Exception("Failed to load doctor's appointments");
    }
  }

  static Future<List<Availability>> fetchDoctorAvailabilities(int doctorID) async {
    var response = await http.get(Uri.parse('$apiAuthRootUrl/doctors/$doctorID/availabilities'));

    if (response.statusCode == 200) {
      var jsonData = jsonDecode(response.body);

      return jsonData.map<Availability>((availability) => Availability.fromJson(availability)).toList();
    }
    else {
      throw Exception("Failed to load doctor's availabilities");
    }
  }

}