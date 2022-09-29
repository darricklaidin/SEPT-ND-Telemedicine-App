import 'dart:convert';
import 'package:http/http.dart' as http;

import 'package:frontend/config/constants.dart';
import 'package:frontend/models/doctor.dart';
import '../models/availability.dart';
import 'doctor_service.dart';


class AvailabilityService {

  static Future getAvailabilityFromJSON(availability) async {
    dynamic tempDoctor = await DoctorService.fetchDoctor(availability['doctorID']);
    if (tempDoctor == "Resource Not Found") {
      return null;
    }
    Doctor doctor = tempDoctor;

    return Availability.fromJson(availability, doctor);
  }

}