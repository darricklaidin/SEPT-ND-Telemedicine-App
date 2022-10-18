import 'package:flutter/material.dart';

import '../models/appointment.dart';
import '../models/doctor.dart';
import '../models/patient.dart';

List<Appointment> mockAppointments = [
  Appointment(
    appointmentID: 1,
    date: DateTime(2022, 08, 25),
    startTime: const TimeOfDay(hour: 16, minute: 0),
    endTime: const TimeOfDay(hour: 16, minute: 30),
    appointmentStatus: "CREATED",
    doctor: Doctor(
      userID: 1,
      firstName: "Doc",
      lastName: "McDoc1",
      email: "Doc@des.com",
      dateOfBirth: DateTime(1999, 08, 25),
      specialty: "GP1",
      accountStatus: true,
    ),
    patient: Patient(
      userID: 2,
      firstName: "Des",
      lastName: "Nim1",
      email: "email@des.com",
      dateOfBirth: DateTime(1999, 08, 25),
      symptoms: "",
      accountStatus: true,
    ),
  ),
  Appointment(
    appointmentID: 2,
    date: DateTime(2022, 08, 26),
    startTime: const TimeOfDay(hour: 17, minute: 0),
    endTime: const TimeOfDay(hour: 17, minute: 30),
    appointmentStatus: "CREATED",
    doctor: Doctor(
      firstName: "Doc",
      lastName: "McDoc2",
      email: "Doc@des.com",
      userID: 1,
      dateOfBirth: DateTime(1998, 08, 26),
      specialty: "GP2",
      accountStatus: true,
    ),
    patient: Patient(
      userID: 2,
      firstName: "Des",
      lastName: "Nim2",
      email: "email@des.com",
      dateOfBirth: DateTime(1998, 08, 26),
      symptoms: "",
      accountStatus: true,
    ),
  ),
  Appointment(
    appointmentID: 3,
    date: DateTime(2022, 08, 27),
    startTime: const TimeOfDay(hour: 18, minute: 0),
    endTime: const TimeOfDay(hour: 18, minute: 30),
    appointmentStatus: "CREATED",
    doctor: Doctor(
      userID: 1,
      firstName: "Doc",
      lastName: "McDoc3",
      email: "Doc@des.com",
      dateOfBirth: DateTime(1997, 08, 27),
      specialty: "GP3",
      accountStatus: true,
    ),
    patient: Patient(
      userID: 2,
      firstName: "Des",
      lastName: "Nim3",
      email: "email@des.com",
      dateOfBirth: DateTime(1997, 08, 27),
      symptoms: "",
      accountStatus: true,
    ),
  ),
];
