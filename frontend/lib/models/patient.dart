import 'package:frontend/models/user.dart';

class Patient extends User {
  Patient({userID, firstName, lastName, email, dateOfBirth, accountStatus})
      : super(
            userID: userID,
            firstName: firstName,
            lastName: lastName,
            email: email,
            dateOfBirth: dateOfBirth);

  factory Patient.fromJson(Map<String, dynamic> json) {
    return Patient(
      userID: json['userID'],
      firstName: json['firstName'],
      lastName: json['lastName'],
      email: json['email'],
      dateOfBirth: DateTime.parse(json['dateOfBirth']),
    );
  }
}
