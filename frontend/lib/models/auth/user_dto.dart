class UserDTO {
  final int userID;
  final String role;

  UserDTO({
    required this.userID,
    required this.role,
  });

  factory UserDTO.fromJson(Map<String, dynamic> json) {
    List<dynamic> roles = json['roles'];

    return UserDTO(
      userID: json['userID'],
      role: roles.elementAt(0)['authority'],
    );
  }
}
