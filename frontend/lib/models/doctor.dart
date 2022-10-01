import 'package:frontend/models/user.dart';
import 'package:frontend/utility.dart';

class Doctor extends User {
  final String specialty;

  Doctor(
      {userID,
      firstName,
      lastName,
      email,
      dateOfBirth,
      required this.specialty})
      : super(
          userID: userID,
          firstName: firstName,
          lastName: lastName,
          email: email,
          dateOfBirth: dateOfBirth,
        );

  factory Doctor.fromJson(Map<String, dynamic> json) {
    return Doctor(
      userID: json['userID'],
      firstName: json['firstName'],
      lastName: json['lastName'],
      email: json['email'],
      dateOfBirth: DateTime.parse(json['dateOfBirth']),
      specialty: json['specialty']['specialtyName'],
    );
  }

  toJson() {
    return {
      'userID': userID,
      'firstName': firstName,
      'lastName': lastName,
      'email': email,
      'dateOfBirth': Utility.dateToStringJSON(dateOfBirth),
      'specialty': specialty,
    };
  }
}
