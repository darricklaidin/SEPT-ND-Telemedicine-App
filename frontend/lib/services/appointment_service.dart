import 'dart:convert';
import 'package:http/http.dart' as http;

import 'package:frontend/config/constants.dart';

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

}
