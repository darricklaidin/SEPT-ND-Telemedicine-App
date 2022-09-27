class User {
  final int userID;
  final String firstName;
  final String lastName;
  final String email;
  final DateTime dateOfBirth;

  User({
    required this.userID,
    required this.firstName,
    required this.lastName,
    required this.email,
    required this.dateOfBirth,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      userID: json['userID'],
      firstName: json['firstName'],
      lastName: json['lastName'],
      email: json['email'],
      dateOfBirth: DateTime.parse(json['dateOfBirth']),
    );
  }

  @override
  String toString() {
    return 'User{userID: $userID\n'
        'firstName: $firstName\n'
        'lastName: $lastName\n'
        'email: $email\n'
        'dateOfBirth: $dateOfBirth\n';
  }
}
