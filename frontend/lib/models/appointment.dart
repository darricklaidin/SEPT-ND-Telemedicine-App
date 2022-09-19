import 'package:flutter/material.dart';
import 'package:frontend/models/doctor.dart';
import 'package:frontend/models/patient.dart';

class Appointment {

  final int appointmentID;
  final DateTime date;
  final TimeOfDay startTime;
  final TimeOfDay endTime;
  final String appointmentStatus;
  final Doctor doctor;
  final Patient patient;

  Appointment({
    required this.appointmentID,
    required this.date,
    required this.startTime,
    required this.endTime,
    required this.appointmentStatus,
    required this.doctor,
    required this.patient,
  });

  factory Appointment.fromJson(Map<String, dynamic> json) {
    return Appointment(
      appointmentID: json['appointmentID'],
      date: DateTime.parse(json['date']),
      startTime: TimeOfDay(hour: int.parse(json['startTime'].split(':')[0]), minute: int.parse(json['startTime'].split(':')[1])),
      endTime: TimeOfDay(hour: int.parse(json['endTime'].split(':')[0]), minute: int.parse(json['endTime'].split(':')[1])),
      appointmentStatus: json['appointmentStatus'],
      doctor: Doctor.fromJson(json['doctor']),
      patient: Patient.fromJson(json['patient']),
    );
  }

  @override
  String toString() {
    return 'Appointment{appointmentID: $appointmentID\n'
        'date: $date\n'
        'startTime: $startTime\n'
        'endTime: $endTime\n'
        'appointmentStatus: $appointmentStatus\n'
        'doctor: $doctor\n'
        'patient: $patient}';
  }

}