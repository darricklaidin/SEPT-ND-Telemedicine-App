import '../models/appointment.dart';
import 'package:frontend/config/constants.dart';

import 'package:http/http.dart' as http;
import 'dart:convert';

import '../models/doctor.dart';
import '../models/patient.dart';
import 'auth_service.dart';
import 'patient_service.dart';

class DoctorService {
  static Future<Doctor> fetchDoctor(int doctorID) async {
    final response = await http
        .get(Uri.parse('$apiAuthRootUrl/doctors/$doctorID'), headers: {
      'Authorization': 'Bearer ${await getJWT()}',
    });

    if (response.statusCode == 200) {
      return Doctor.fromJson(jsonDecode(response.body));
    } else {
      // return Doctor();
      throw Exception('Failed to load doctor profile');
    }
  }

  static Future<List<Appointment>> fetchDoctorAppointments() async {
    int doctorID = await getUserIdFromStorage();

    var response = await http.get(Uri.parse(
        '$apiBookingRootUrl/appointments/doctor/$doctorID?sort=date&sort=startTime'))
        .timeout(const Duration(seconds: 5));

    if (response.statusCode == 200) {
      List<dynamic> jsonData = jsonDecode(response.body)['content'];
      List<Appointment> appointments = [];
      for (dynamic appointment in jsonData) {
        appointments.add(await getAppointmentFromJSON(appointment));
      }
      return appointments;
    } else {
      throw Exception("Failed to load doctor's appointments");
    }
  }

  static Future<Appointment> getAppointmentFromJSON(appointment) async {
    Doctor doctor = await fetchDoctor(appointment['doctorID']);
    Patient patient =
        await PatientService.fetchPatient(appointment['patientID']);

    return Appointment.fromJson(appointment, doctor, patient);
  }

}
