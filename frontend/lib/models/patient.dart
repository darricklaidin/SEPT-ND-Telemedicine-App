import 'package:frontend/models/user.dart';
import 'package:intl/intl.dart';

class Patient extends User {

  Patient({userID, firstName, lastName, email, dateOfBirth, accountStatus})
      : super(userID: userID, firstName: firstName, lastName: lastName, email: email,
      dateOfBirth: dateOfBirth, accountStatus: accountStatus);

  factory Patient.fromJson(Map<String, dynamic> json) {
    return Patient(
      userID: json['userID'],
      firstName: json['firstName'],
      lastName: json['lastName'],
      email: json['email'],
      dateOfBirth: DateTime.parse(json['dateOfBirth']),
      accountStatus: json['accountStatus'],
    );
  }

  // Map<String, dynamic> toJson() {
  //   return {
  //     'userID': userID,
  //     'firstName': firstName,
  //     'lastName': lastName,
  //     'email': email,
  //     'dateOfBirth': dateOfBirth,
  //     'accountStatus': accountStatus,
  //   };
  // }

  @override
  String toString() {
    return super.toString();
  }

  toJson() {
    return {
      'userID': userID,
      'firstName': firstName,
      'lastName': lastName,
      'email': email,
      'dateOfBirth': DateFormat('yyyy-MM-dd').format(dateOfBirth),
      'accountStatus': accountStatus,
    };
  }
}
