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

  //BRYAN ATTEMPTING DOCTOR ADMIN LIST
  static Future<List<Doctor>> fetchDoctorList() async {

    //initialize list of patients;
    List<Doctor> doctors = [];

    //fetching the data
    var response = await http.get(Uri.parse('$apiAuthRootUrl/doctors'), headers: {
      'Authorization': 'Bearer ${await getJWT()}'});

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


  static Future<List<Appointment>> fetchDoctorAppointments() async {
    int doctorID = await getUserIdFromStorage();

    var response = await http.get(Uri.parse(
        '$apiBookingRootUrl/appointments/doctor/$doctorID?sort=date&sort=startTime'));

    if (response.statusCode == 200) {
      List<dynamic> jsonData = jsonDecode(response.body);
      List<Appointment> appointments = [];
      for (dynamic a in jsonData) {
        appointments.add(await getAppointmentFromJSON(a));
      }
      return appointments;
    } else {
      // TODO: display error message
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
