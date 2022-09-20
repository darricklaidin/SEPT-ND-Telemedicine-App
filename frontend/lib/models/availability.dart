
import 'package:frontend/models/doctor.dart';

class Availability {
  int? availabilityID;
  int? dayOfWeek;
  DateTime? startTime;
  DateTime? endTime;
  Doctor? doctor;

  Availability({
    this.availabilityID,
    this.dayOfWeek,
    this.startTime,
    this.endTime,
    this.doctor,
  });

  factory Availability.fromJson(Map<String, dynamic> json) {
    return Availability(
      availabilityID: json['availabilityID'],
      dayOfWeek: json['dayOfWeek'],
      startTime: DateTime.parse(json['startTime']),
      endTime: DateTime.parse(json['endTime']),
      doctor: Doctor.fromJson(json['doctor']),
    );
  }

}