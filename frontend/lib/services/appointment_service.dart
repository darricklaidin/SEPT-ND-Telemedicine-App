import 'dart:convert';

import 'package:frontend/config/constants.dart';
import 'package:frontend/models/appointment.dart';
import 'package:http/http.dart' as http;

class AppointmentService {

  static Future deleteAppointment(int? appointmentID) async {
    var response = await http.delete(Uri.parse('$apiRootUrl/appointments/$appointmentID'));
    if (response.statusCode == 200) {
      print("Deleted appointment $appointmentID");
    }
    else {
      throw Exception('Failed to delete appointment with id: $appointmentID');
    }
  }

  static Future bookAppointment(Appointment appointment) async {
    var response = await http.post(Uri.parse('$apiRootUrl/appointments'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(appointment.toJson()),
    );

    if (response.statusCode == 200) {
      return "success";
    }
    else {
      if (response.body.contains("Time Clash")) {
        return "time clash";
      }
      else if (response.body.contains("Doctor Unavailable")) {
        return "doctor unavailable";
      }
      else {
        return "error";
      }
    }

  }

  static Future updateAppointment(int appointmentID, Appointment appointment) async {
    var response = await http.put(Uri.parse('$apiRootUrl/appointments/$appointmentID'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(appointment.toJson()),
    );

    if (response.statusCode == 200) {
      return "success";
    }
    else {
      if (response.body.contains("Time Clash")) {
        return "time clash";
      }
      else if (response.body.contains("Doctor Unavailable")) {
        return "doctor unavailable";
      }
      else {
        return "error";
      }
    }
  }

}