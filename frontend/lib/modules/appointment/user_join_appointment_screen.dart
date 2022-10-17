import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:frontend/modules/chat/chat_screen.dart';

class UserJoinAppointment extends StatefulWidget {
  final Function delete;
  final Function handleTabSelection;
  final String name;
  final int userID;
  final int otherUserID;
  final bool isPatient;

  const UserJoinAppointment({
    Key? key,
    required this.delete,
    required this.name,
    required this.userID,
    required this.handleTabSelection,
    required this.otherUserID,
    required this.isPatient,
  }) : super(key: key);

  @override
  State<UserJoinAppointment> createState() => _UserJoinAppointmentState();
}

class _UserJoinAppointmentState extends State<UserJoinAppointment> {
  @override
  Widget build(BuildContext context) {
    Color secondaryThemeColor = Theme.of(context).colorScheme.secondary;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Appointment'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            const Icon(CupertinoIcons.profile_circled, size: 40),
            Text(widget.name),
            const SizedBox(height: 50),
            ElevatedButton(
              onPressed: () {
                widget.delete();
                Navigator.pop(context);
              },
              style: ButtonStyle(
                backgroundColor: MaterialStateProperty.all(Colors.red),
              ),
              child: const Text(
                "Cancel",
                style: TextStyle(fontWeight: FontWeight.bold),
              ),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => ChatScreen(
                            handleTabSelection: widget.handleTabSelection,
                            doctorID: widget.isPatient
                                ? widget.otherUserID
                                : widget.userID,
                            isPatient: widget.isPatient,
                            patientID: widget.isPatient
                                ? widget.userID
                                : widget.otherUserID,
                          )),
                );
              },
              style: ButtonStyle(
                backgroundColor: MaterialStateProperty.all(secondaryThemeColor),
              ),
              child: const Text(
                "Join Chat",
                style: TextStyle(fontWeight: FontWeight.bold),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
