import 'dart:convert';
import 'package:http/http.dart' as http;

import 'package:frontend/config/constants.dart';
import 'package:frontend/models/doctor.dart';
import '../models/availability.dart';
import 'doctor_service.dart';


class AvailabilityService {

  static Future createAvailability(Availability availability) async {

    final response = await http.post(Uri.parse('$apiBookingRootUrl/availabilities'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode(availability.toJson()));

    print(response.body);

    if (response.statusCode == 200) {
      return Availability.fromJson(jsonDecode(response.body), availability.doctor);
    } else if (jsonDecode(response.body)['message'] == "Resource Already Exists") {
      return "Resource Already Exists";
    } else if (jsonDecode(response.body)['message'] == "Invalid Time Range") {
      return "Invalid Time Range";
    } else {
      throw Exception('Failed to create availability');
    }

  }

  static Future updateAvailability(int availabilityID, Availability availability) async {

    final response = await http.put(Uri.parse('$apiBookingRootUrl/availabilities/$availabilityID'),
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode(availability.toJson()));

    if (response.statusCode == 200) {
      return Availability.fromJson(jsonDecode(response.body), availability.doctor);
    } else if (jsonDecode(response.body)['message'] == "Invalid Time Range") {
      return "Invalid Time Range";
    } else {
      throw Exception('Failed to update availability');
    }

  }

  static Future deleteAvailability(int availabilityID) async {

    final response = await http.delete(Uri.parse('$apiBookingRootUrl/availabilities/$availabilityID'),
        headers: {
          'Content-Type': 'application/json',
        });

    if (response.statusCode == 200) {
      return "Success";
    } else if (jsonDecode(response.body)['message'] == "Resource Not Found") {
      return "Resource Not Found";
    } else {
      throw Exception('Failed to delete availability');
    }

  }


  static Future getAvailabilityFromJSON(availability) async {

    dynamic tempDoctor = await DoctorService.fetchDoctor(availability['doctorID']);
    if (tempDoctor == "Resource Not Found") {
      return null;
    }
    Doctor doctor = tempDoctor;

    return Availability.fromJson(availability, doctor);
  }

}