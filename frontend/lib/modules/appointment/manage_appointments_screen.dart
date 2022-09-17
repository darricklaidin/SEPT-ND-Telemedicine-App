import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:frontend/models/appointment_card.dart';
import 'package:frontend/models/appointment.dart';

class ManageAppointmentsScreen extends StatefulWidget {
  ManageAppointmentsScreen({Key? key}) : super(key: key);

  @override
  _ManageAppointmentsScreenState createState() =>
      _ManageAppointmentsScreenState();
}

class _ManageAppointmentsScreenState extends State<ManageAppointmentsScreen> {

  // final List appointments = [
  //   Appointment(appointmentID: 1, date: "1 Jan 2022", startTime: "7:31 PM",
  //       endTime: "8:31 PM", appointmentStatus: "Upcoming",
  //       doctorID: 1, patientID: 2),
  // ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(vertical: 0, horizontal: 20),
        child: Column(
          children: <Widget>[
            Align(
              alignment: Alignment.centerRight,
              child: IconButton(
                splashRadius: 20.0,
                iconSize: 35.0,
                icon: const Icon(CupertinoIcons.profile_circled),
                onPressed: () {},
              )
            ),
            const Align(
              alignment: Alignment.centerLeft,
              child: Text(
                "Appointments",
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            Expanded(
              child: ListView(
                children: <Widget>[
                  AppointmentCard(
                      name: "Bryan Hong",
                      age: 30,
                      startDateTime: DateTime.now(),
                      endDateTime: DateTime.now().add(const Duration(hours: 1))
                  ),
                  const SizedBox(height: 20,),
                  AppointmentCard(
                      name: "John Krasinkiussy",
                      age: 21,
                      startDateTime: DateTime.now(),
                      endDateTime: DateTime.now().add(const Duration(hours: 1))
                  ),
                ],
              ),
            )
          ],
        ),
      )
    );
  }
}
