import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class PatientJoinAppointment extends StatefulWidget {
  final Function delete;
  final String name;

  const PatientJoinAppointment({Key? key, required this.delete, required this.name}) : super(key: key);

  @override
  State<PatientJoinAppointment> createState() => _PatientJoinAppointmentState();
}

class _PatientJoinAppointmentState extends State<PatientJoinAppointment> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Join Appointment'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            const Icon(CupertinoIcons.profile_circled, size: 40),
            Text(
              widget.name,
            ),
            const SizedBox(height: 50),
            ElevatedButton(
              onPressed: () {
                widget.delete();
                },  // TODO: Cancel appointment
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.red,
              ),
              child: const Text("Cancel"),
            ),
            ElevatedButton(
              onPressed: () {},  // TODO: Join Chat Page
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.amber,
              ),
              child: const Text("Join Chat"),
            ),
          ],
        ),
      ),
    );
  }
}
