import 'dart:async';
import 'dart:convert';

import '../models/doctor.dart';
import 'package:http/http.dart' as http;
import '../models/user.dart';

Future<Doctor> getDoctor() async {
  final response =
      await http.get(Uri.parse('http://10.0.0.82:8080/api/doctors/2'));
  if (response.statusCode == 200) {
    return Doctor.fromJson(jsonDecode(response.body));
  } else {
    throw Exception('Failed to load doctor profile');
  }
}

Future<User> getPatient() async {
  final response =
      await http.get(Uri.parse('http://10.0.0.82:8080/api/patients/1'));
  if (response.statusCode == 200) {
    return User.fromJson(jsonDecode(response.body));
  } else {
    throw Exception('Failed to load patient profile');
  }
}
