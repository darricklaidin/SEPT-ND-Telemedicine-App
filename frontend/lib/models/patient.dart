import 'package:frontend/models/user.dart';

import '../utility.dart';

class Patient extends User {
  String? symptoms;

  Patient({userID, firstName, lastName, email, dateOfBirth, accountStatus, required this.symptoms})
      : super(
            userID: userID,
            firstName: firstName,
            lastName: lastName,
            email: email,
            dateOfBirth: dateOfBirth,
            accountStatus: accountStatus);

  factory Patient.fromJson(Map<String, dynamic> json) {
    return Patient(
      userID: json['userID'],
      firstName: json['firstName'],
      lastName: json['lastName'],
      email: json['email'],
      dateOfBirth: DateTime.parse(json['dateOfBirth']),
      accountStatus: json['accountStatus'],
      symptoms: json['symptoms'],
    );
  }

  toJson(String? newPassword) {
    return {
      'userID': userID,
      'firstName': firstName,
      'lastName': lastName,
      'email': email,
      'password': newPassword,
      'dateOfBirth': Utility.dateToStringJSON(dateOfBirth),
      'accountStatus': accountStatus,
      'symptoms': symptoms,
    };
  }

}
