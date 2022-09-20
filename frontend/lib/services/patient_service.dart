import 'package:frontend/services/appointment_service.dart';

import '../models/appointment.dart';
import 'package:frontend/config/constants.dart';

import 'package:http/http.dart' as http;
import 'dart:convert';

class PatientService {

  static Future<List<Appointment>> fetchPatientAppointments(int patientID) async {

    var response = await http.get(Uri.parse('$apiRootUrl/patients/$patientID/appointments?sort=datetime'));

    if (response.statusCode == 200) {
      var jsonData = jsonDecode(response.body);

      List<Appointment> appointments = jsonData.map<Appointment>((appointment) =>
          Appointment.fromJson(appointment)).toList();

      List<Appointment> validAppointments = List<Appointment>.empty(growable: true);

      // If appointment date has passed, do not include the appointment in list and set status in db to "completed"
      for (Appointment appointment in appointments) {
        if (!(appointment.date.isBefore(DateTime.now()) ||
            (appointment.date.isAtSameMomentAs(DateTime.now()) &&
                appointment.endTime.hour < DateTime.now().hour))) {
          validAppointments.add(appointment);
        }
        else {
          appointment.appointmentStatus = "COMPLETED";
          await AppointmentService.updateAppointment(appointment.appointmentID!, appointment);
        }
      }

      return validAppointments;
    }
    else {
      throw Exception("Failed to load patient's appointments");
    }
  }

}