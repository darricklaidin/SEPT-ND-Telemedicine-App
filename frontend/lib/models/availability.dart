
import 'package:flutter/material.dart';
import 'package:frontend/models/doctor.dart';
import 'package:frontend/utility.dart';

class Availability {
  int? availabilityID;
  int? dayOfWeek;
  TimeOfDay? startTime;
  TimeOfDay? endTime;
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
      dayOfWeek: Utility.convertDayOfWeekToInt(json['dayOfWeek']),
      startTime: TimeOfDay(hour: int.parse(json['startTime'].split(':')[0]), minute: int.parse(json['startTime'].split(':')[1])),
      endTime: TimeOfDay(hour: int.parse(json['endTime'].split(':')[0]), minute: int.parse(json['endTime'].split(':')[1])),
      doctor: Doctor.fromJson(json['doctor']),
    );
  }



}