import 'dart:convert';
import 'package:http/http.dart' as http;

import '../models/specialty.dart';
import 'package:frontend/config/constants.dart';

import 'auth_service.dart';


class SpecialtyService {

  static Future getSpecialties() async {
    var response = await http.get(
      Uri.parse('$apiAuthRootUrl/specialties'),
      headers: {
        'Authorization': 'Bearer ${await getJWT()}',
      },
    );

    if (response.statusCode == 200) {
      var specialties = json.decode(response.body)['content'];
      return specialties.map((specialty) => Specialty.fromJson(specialty)).toList();
    } else {
      throw Exception('Failed to load specialties');
    }
  }

  static Future createSpecialty(Specialty specialty) async {

    var response = await http.post(
      Uri.parse('$apiAuthRootUrl/specialties'),
      headers: {
        'Authorization': 'Bearer ${await getJWT()}',
        'Content-Type': 'application/json',
      },
      body: jsonEncode(specialty.toJson()),
    );

    Map<String, dynamic> decodedResponse = jsonDecode(response.body);

    print(decodedResponse);

    if (response.statusCode == 200) {
      return Specialty.fromJson(decodedResponse);
    } else if (decodedResponse['message'] == "Resource Already Exists") {
      throw Exception('Specialty already exists');
    } else {
      throw Exception('Failed to create specialty');
    }

  }

}