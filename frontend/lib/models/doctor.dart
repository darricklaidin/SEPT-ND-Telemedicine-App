import 'package:frontend/models/user.dart';
import 'package:intl/intl.dart';

class Doctor extends User {
  final String specialty;

  Doctor({userID, firstName, lastName, email, dateOfBirth, accountStatus, required this.specialty})
      : super(userID: userID, firstName: firstName, lastName: lastName, email: email,
      dateOfBirth: dateOfBirth, accountStatus: accountStatus);

  factory Doctor.fromJson(Map<String, dynamic> json) {
    return Doctor(
      userID: json['userID'],
      firstName: json['firstName'],
      lastName: json['lastName'],
      email: json['email'],
      dateOfBirth: DateTime.parse(json['dateOfBirth']),
      accountStatus: json['accountStatus'],
      specialty: json['specialty']['specialtyName'],
    );
  }

  toJson() {
    return {
      'userID': userID,
      'firstName': firstName,
      'lastName': lastName,
      'email': email,
      'dateOfBirth': DateFormat('yyyy-MM-dd').format(dateOfBirth),
      'accountStatus': accountStatus,
      'specialty': specialty,
    };
  }

}
