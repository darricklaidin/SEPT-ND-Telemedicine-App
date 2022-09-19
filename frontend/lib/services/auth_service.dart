import 'dart:async';
import 'dart:convert';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

import '../config/constants.dart';
import '../models/auth/user_dto.dart';
import 'package:http/http.dart' as http;

const storage = FlutterSecureStorage();

Future<UserDTO?> checkAuth() async {
  UserDTO? res;
  // get jwt from storage and validate it
  String jwt = await getJWT();
  if (jwt != '') res = await validateJWT(jwt);

  return res;
}

Future<String> getJWT() async {
  var jwt = await storage.read(key: "jwt");
  if (jwt == null) return "";
  return jwt;
}

Future<UserDTO?> validateJWT(String token) async {
  UserDTO? res;
  Uri url = Uri.parse('$apiAuthRootUrl/auth/validate');
  final response = await http.get(url, headers: {
    'Authorization': 'Bearer $token',
  });
  if (response.statusCode == 200) {
    res = UserDTO.fromJson(jsonDecode(response.body));
  }
  return res;
}
