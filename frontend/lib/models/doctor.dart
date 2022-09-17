import 'package:frontend/models/user.dart';

class Doctor extends User {
  final String specialty;

  Doctor({id, firstName, lastName, email, required this.specialty})
      : super(id: id, firstName: firstName, lastName: lastName, email: email);

  factory Doctor.fromJson(Map<String, dynamic> json) {
    return Doctor(
      id: json['userID'],
      firstName: json['firstName'],
      lastName: json['lastName'],
      email: json['email'],
      specialty: json['specialty']['specialtyName'],
    );
  }
}
