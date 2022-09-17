class User {
  final int id;
  final String firstName;
  final String lastName;
  final String email;

  User(
      {required this.id,
      required this.firstName,
      required this.lastName,
      required this.email});

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['userID'],
      firstName: json['firstName'],
      lastName: json['lastName'],
      email: json['email'],
    );
  }
}
