class Specialty {
  final int specialtyID;
  final String specialtyName;

  Specialty({
    required this.specialtyName, required this.specialtyID,
  });

  factory Specialty.fromJson(Map<String, dynamic> json) {
    return Specialty(
      specialtyID: json['specialtyID'],
      specialtyName: json['specialtyName'],
    );
  }

  toJson() {
    return {
      'specialtyName': specialtyName,
    };
  }

}