import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

import 'package:frontend/models/user.dart';
import '../config/constants.dart';
import '../models/api_response.dart';
import '../models/auth/user_dto.dart';

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
  }).timeout(Duration(seconds: 3));
  if (response.statusCode == 200) {
    res = UserDTO.fromJson(jsonDecode(response.body));
  }
  return res;
}

Future<ApiResponse> loginUser(String email, String password) async {
  // setup request
  Uri url = Uri.parse('$apiAuthRootUrl/auth/login');
  Map data = {'username': email, 'password': password};
  var body = json.encode(data);

  final response = await http.post(url,
      headers: {"Content-Type": "application/json"}, body: body)
      .timeout(const Duration(seconds: 3));
  Map<String, dynamic> decodedResponse = jsonDecode(response.body);

  ApiResponse res = ApiResponse();
  if (response.statusCode == 200 && decodedResponse['success']) {
    // set jwt in storage
    storage.write(key: "jwt", value: decodedResponse['accessToken']);
    UserDTO temp = UserDTO.fromJson(decodedResponse);
    // set user details in storage
    storage.write(key: "userId", value: temp.userID.toString());
    storage.write(key: "role", value: temp.role);

    res.success = true;
  } else {
    // get error message
    res.msg = decodedResponse['message'] ?? 'Error';
  }
  return res;
}

Future<void> logoutUser() async {
  await storage.deleteAll();
}

Future<int> getUserIdFromStorage() async {
  return int.parse(await storage.read(key: "userId") ?? "-1");
}

Future<String> getUserRoleFromStorage() async {
  return await storage.read(key: "role") ?? "NO ROLE";
}

Future<User?> getUserFromStorage() async {
  int userID = await getUserIdFromStorage();
  String userRole = await getUserRoleFromStorage();
  String userRolePath;

  switch (userRole) {
    case "ADMIN":
      userRolePath = "admins";
      break;
    case "PATIENT":
      userRolePath = "patients";
      break;
    case "DOCTOR":
      userRolePath = "doctors";
      break;
    default:
      userRolePath = "invalidUserRolePath";
  }

  Uri url = Uri.parse('$apiAuthRootUrl/$userRolePath/$userID');
  String token = await getJWT();

  final response = await http.get(url,
      headers: {
        'Authorization': 'Bearer $token',
      });

  if (response.statusCode == 200) {
    return User.fromJson(jsonDecode(response.body));
  } else {
    return null;
  }

}