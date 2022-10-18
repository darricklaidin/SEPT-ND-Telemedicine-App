import 'package:frontend/models/availability.dart';
import 'package:frontend/services/appointment_service.dart';
import 'package:frontend/services/availability_service.dart';

import '../models/appointment.dart';
import 'package:frontend/config/constants.dart';

import 'package:http/http.dart' as http;
import 'dart:convert';

import '../models/doctor.dart';
import 'auth_service.dart';

class DoctorService {
  static Future<List<Doctor>> fetchAllDoctors() async {
    final response = await http
        .get(Uri.parse('$apiAuthRootUrl/doctors?sort=firstName'), headers: {
      'Authorization': 'Bearer ${await AuthService.getJWT()}',
    }).timeout(const Duration(seconds: 5));

    if (response.statusCode == 200) {
      return jsonDecode(response.body)['content']
          .map<Doctor>((doctor) => Doctor.fromJson(doctor))
          .toList();
    } else {
      throw Exception('Failed to load doctors');
    }
  }

  static Future<List<Doctor>> fetchDoctorList() async {

    //initialize list of patients;
    List<Doctor> doctors = [];

    //fetching the data
    var response = await http.get(Uri.parse('$apiAuthRootUrl/doctors'), headers: {
      'Authorization': 'Bearer ${await AuthService.getJWT()}'});

    if (response.statusCode == 200) {
      List<dynamic> jsonData = jsonDecode(response.body)["content"];  // list of patients //response.body

      for (dynamic doctor in jsonData) {
        doctors.add(Doctor.fromJson(doctor)); //give Doctor Object
      }
      return doctors;

    } else {
      throw Exception("Failed to load patient's appointments");
    }
  }


  static Future fetchDoctor(int doctorID) async {
    final response = await http
        .get(Uri.parse('$apiAuthRootUrl/doctors/$doctorID'), headers: {
      'Authorization': 'Bearer ${await AuthService.getJWT()}',
    });

    if (response.statusCode == 200) {
      return Doctor.fromJson(jsonDecode(response.body));
    } else if (response.statusCode == 404) {
      return jsonDecode(response.body)['message'];
    } else {
      throw Exception('Failed to load doctor profile');
    }
  }

  Future<List<Appointment>> fetchDoctorAppointments(int doctorID) async {
    var response = await http
        .get(Uri.parse(
            '$apiBookingRootUrl/appointments/doctor/$doctorID?sort=date&sort=startTime'))
        .timeout(const Duration(seconds: 5));

    if (response.statusCode == 200) {
      List<dynamic> jsonData = jsonDecode(response.body)['content'];
      List<Appointment> appointments = [];
      for (dynamic appointment in jsonData) {
        Appointment? tempAppointment =
            await AppointmentService.getAppointmentFromJSON(appointment);
        if (tempAppointment != null) {
          appointments.add(tempAppointment);
        } else {
          // TODO: Appointment should be deleted from database
        }
      }
      return appointments;
    } else {
      throw Exception("Failed to load doctor's appointments");
    }
  }

  static Future<List<Availability>> fetchDoctorAvailabilities(
      int doctorID) async {
    final response = await http
        .get(Uri.parse(
            '$apiBookingRootUrl/availabilities/doctor/$doctorID?sort=dayOfWeek'))
        .timeout(const Duration(seconds: 3));

    if (response.statusCode == 200) {
      List jsonData = jsonDecode(response.body)['content'];

      List<Availability> availabilities = [];
      for (dynamic availability in jsonData) {
        availabilities.add(
            await AvailabilityService.getAvailabilityFromJSON(availability));
      }

      return availabilities;
    } else {
      throw Exception('Failed to load availabilities');
    }
  }

  /// Update the doctor with the specified [doctorID].
  /// Returns the updated [doctor] if successful, otherwise returns the error message.
  /// Leave [newPassword] as [null] if you don't want to change the password.
  static Future updateDoctor(
      int doctorID, Doctor doctor, String? newPassword, int specialtyID) async {
    // if new password is null, use old password
    var response =
        await http.put(Uri.parse('$apiAuthRootUrl/doctors/$doctorID'),
            headers: {
              'Authorization': 'Bearer ${await AuthService.getJWT()}',
              'Content-Type': 'application/json',
            },
            body: jsonEncode(doctor.toJson(newPassword, specialtyID)));

    Map<String, dynamic> decodedResponse = jsonDecode(response.body);

    if (response.statusCode == 200) {
      return "Success";
    } else if (decodedResponse['message'] == "Resource Already Exists") {
      throw Exception("A user with that email already exists");
    } else {
      throw Exception('Failed to update doctor profile');
    }
  }
}
