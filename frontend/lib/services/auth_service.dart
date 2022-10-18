import 'dart:async';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

import '../config/constants.dart';
import '../models/api_response.dart';
import '../models/auth/user_dto.dart';
import '../models/doctor.dart';
import '../models/patient.dart';

const storage = FlutterSecureStorage();

class AuthService {
  Future<UserDTO?> checkAuth() async {
    UserDTO? res;
    // get jwt from storage and validate it
    String jwt = await getJWT();
    if (jwt != '') res = await validateJWT(jwt);

    return res;
  }

  static Future<String> getJWT() async {
    var jwt = await storage.read(key: "jwt");
    if (jwt == null) return "";
    return jwt;
  }

  static Future<UserDTO?> validateJWT(String token) async {
    UserDTO? res;
    Uri url = Uri.parse('$apiAuthRootUrl/auth/validate');
    final response = await http.get(url, headers: {
      'Authorization': 'Bearer $token',
    }).timeout(const Duration(seconds: 3));
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

    final response = await http
        .post(url, headers: {"Content-Type": "application/json"}, body: body)
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

  Future<ApiResponse> registerUser(String firstName, String lastName,
      String email, String password, String dateOfBirth) async {
    Uri url = Uri.parse('$apiAuthRootUrl/auth/signup');
    Map data = {
      'firstName': firstName,
      'lastName': lastName,
      'email': email,
      'password': password,
      'dateOfBirth': dateOfBirth
    };
    var body = json.encode(data);

    final response = await http.post(url,
        headers: {"Content-Type": "application/json"}, body: body);
    Map<String, dynamic> decodedResponse = jsonDecode(response.body);

    ApiResponse res = ApiResponse();
    if (response.statusCode == 200) {
      await loginUser(decodedResponse['email'], password);
      res.success = true;
    } else {
      // get error message
      res.msg = "A user with that email already exists.";
    }
    return res;
  }

  Future<ApiResponse> registerDoctor(
      String firstName,
      String lastName,
      String email,
      String password,
      String dateOfBirth,
      int specialtyID) async {
    Uri url = Uri.parse('$apiAuthRootUrl/auth/signup-doctor');
    Map data = {
      'firstName': firstName,
      'lastName': lastName,
      'email': email,
      'password': password,
      'dateOfBirth': dateOfBirth,
      'specialtyID': specialtyID
    };

    var body = json.encode(data);

    final response = await http.post(url,
        headers: {"Content-Type": "application/json"}, body: body);

    ApiResponse res = ApiResponse();
    if (response.statusCode == 200) {
      res.success = true;
    } else {
      // get error message
      res.msg = "A user with that email already exists.";
    }
    return res;
  }

  Future<void> logoutUser() async {
    await storage.deleteAll();
  }

  static Future<int> getUserIdFromStorage() async {
    return int.parse(await storage.read(key: "userId") ?? "-1");
  }

  Future<String> getUserRoleFromStorage() async {
    return await storage.read(key: "role") ?? "NO ROLE";
  }

  Future getUserFromStorage() async {
    int userID = await getUserIdFromStorage();
    String userRole = await getUserRoleFromStorage();
    String userRolePath;

    if (userRole == "ADMIN") {
      return "ADMIN";
    }

    switch (userRole) {
      case "PATIENT":
        userRolePath = "patients";
        break;
      case "DOCTOR":
        userRolePath = "doctors";
        break;
      default:
        userRolePath = "invalidUserRolePath";
    }

    if (userRolePath == "invalidUserRolePath") {
      return null;
    }

    Uri url = Uri.parse('$apiAuthRootUrl/$userRolePath/$userID');
    String token = await getJWT();

    final response = await http.get(url, headers: {
      'Authorization': 'Bearer $token',
    });

    if (response.statusCode == 200) {
      if (userRole == "PATIENT") {
        return Patient.fromJson(jsonDecode(response.body));
      } else if (userRole == "DOCTOR") {
        return Doctor.fromJson(jsonDecode(response.body));
      }
    } else {
      return null;
    }
  }
}
