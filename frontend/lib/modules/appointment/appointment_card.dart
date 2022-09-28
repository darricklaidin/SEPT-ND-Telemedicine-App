import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:frontend/modules/patient/user_join_appointment.dart';
import 'package:frontend/services/appointment_service.dart';

class AppointmentCard extends StatefulWidget {
  final int appointmentID;
  final String name;
  final int age;
  final String date;
  final String startTime;
  final String endTime;

  final Function delete;
  final Function handleTabSelection;
  final Function reload;

  const AppointmentCard(
      {
        Key? key,
        required this.appointmentID,
        required this.name,
        required this.age,
        required this.date,
        required this.startTime,
        required this.endTime,
        required this.delete,
        required this.handleTabSelection,
        required this.reload,})
      : super(key: key);

  @override
  _AppointmentCardState createState() => _AppointmentCardState();
}

class _AppointmentCardState extends State<AppointmentCard> {
  @override
  Widget build(BuildContext context) {
    Color primaryThemeColor = Theme.of(context).colorScheme.primary;
    Color secondaryThemeColor = Theme.of(context).colorScheme.secondary;
    Color errorThemeColor = Theme.of(context).colorScheme.error;

    return Card(
      color: primaryThemeColor,
      child: Padding(
        padding: const EdgeInsets.all(20.0),
        child: Column(
          children: <Widget>[
            Row(
              children: <Widget>[
                const Icon(
                  CupertinoIcons.profile_circled,
                  color: Colors.white,
                  size: 40,
                ),
                const SizedBox(width: 10),
                SizedBox(
                  width: 100,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[
                      Text(
                          // if name exceeds length
                          widget.name.length > 13
                              ? "${widget.name.substring(0, 13)}..."
                              : widget.name,
                          style: const TextStyle(
                            color: Colors.white,
                            fontWeight: FontWeight.bold,
                          )),
                      Text("Age: ${widget.age.toString()}",
                          style: const TextStyle(
                            color: Colors.white,
                          )),
                    ],
                  ),
                ),
                const SizedBox(width: 25),
                ElevatedButton(
                  onPressed: () {
                    widget.delete();
                  },
                  style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(Colors.red),
                  ),
                  child: const Text("Cancel",
                      style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                      )),
                ),
                const SizedBox(width: 20),
                IconButton(
                  onPressed: () async {
                    // Before navigating to the join appointment screen, check
                    // if the appointment still exists in the database
                    if (await AppointmentService.findAppointmentByID(widget.appointmentID) == "Resource Not Found") {
                      ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: const Text("Appointment no longer exists"),
                            behavior: SnackBarBehavior.floating,
                            margin: const EdgeInsets.only(bottom: 10.0),
                            duration: const Duration(seconds: 2),
                            backgroundColor: errorThemeColor,

                          ));
                      widget.reload();
                    } else {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (context) => UserJoinAppointment(
                              delete: widget.delete,
                              handleTabSelection: widget.handleTabSelection,
                              name: widget.name,
                            )),
                      );
                    }



                  },
                  icon: const Icon(CupertinoIcons.right_chevron),
                  color: Colors.white,
                ),
              ],
            ),
            const SizedBox(
              height: 50,
              child: Divider(
                color: Colors.white,
                thickness: 1.5,
              ),
            ), // Replace with line
            Row(
              children: <Widget>[
                const Icon(
                  CupertinoIcons.calendar,
                  color: Colors.white,
                ),
                const SizedBox(
                  width: 10,
                ),
                Text(
                  widget.date,
                  style: const TextStyle(
                    color: Colors.white,
                  ),
                ),
                const SizedBox(
                  width: 40,
                ),
                const Icon(
                  CupertinoIcons.time,
                  color: Colors.white,
                ),
                const SizedBox(
                  width: 10,
                ),
                Text(
                  "${widget.startTime} - ${widget.endTime}",
                  style: const TextStyle(
                    color: Colors.white,
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
