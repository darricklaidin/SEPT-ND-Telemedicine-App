class User {
  final int userID;
  final String firstName;
  final String lastName;
  final String email;
  final DateTime dateOfBirth;
  final String accountStatus;

  User(
      {required this.userID,
      required this.firstName,
      required this.lastName,
      required this.email,
      required this.dateOfBirth,
      required this.accountStatus});

  @override
  String toString() {
    return 'User{userID: $userID\n'
        'firstName: $firstName\n'
        'lastName: $lastName\n'
        'email: $email\n'
        'dateOfBirth: $dateOfBirth\n'
        'accountStatus: $accountStatus}';
  }
}
