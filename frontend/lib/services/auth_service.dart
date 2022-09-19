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
  String url = '${apiAuthRootUrl}api/auth/validate';
  final response = await http.get(Uri.parse(url));
  if (response.statusCode == 200) {
    return UserDTO.fromJson(jsonDecode(response.body));
  } else {
    return null;
  }
}
