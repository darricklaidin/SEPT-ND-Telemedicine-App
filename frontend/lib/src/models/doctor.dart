import 'package:frontend/src/models/user.dart';

class Doctor extends User {
  final String specialty;

  Doctor({id, firstName, lastName, email, required this.specialty})
      : super(id: id, firstName: firstName, lastName: lastName, email: email);
}
