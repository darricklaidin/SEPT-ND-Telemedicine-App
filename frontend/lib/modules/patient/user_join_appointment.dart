import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:frontend/config/constants.dart';


class UserJoinAppointment extends StatefulWidget {
  final Function delete;
  final Function handleTabSelection;
  final String name;

  const UserJoinAppointment(
      {Key? key, required this.delete, required this.name, required this.handleTabSelection})
      : super(key: key);

  @override
  State<UserJoinAppointment> createState() => _UserJoinAppointmentState();
}

class _UserJoinAppointmentState extends State<UserJoinAppointment> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    Color primaryThemeColor = Theme.of(context).colorScheme.primary;
    Color secondaryThemeColor = Theme.of(context).colorScheme.secondary;
    Color errorThemeColor = Theme.of(context).colorScheme.error;

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
            Text(
              widget.name,
            ),
            const SizedBox(height: 50),
            ElevatedButton(
              onPressed: () {
                widget.delete();
                Navigator.pop(context);
              },
              style: ButtonStyle(
                backgroundColor: MaterialStateProperty.all(Colors.red),
              ),
              child: const Text("Cancel", style: TextStyle(fontWeight: FontWeight.bold),),
            ),
            ElevatedButton(
              onPressed: () {
                // This method pushes a new screen to the stack but does not update the bottom nav bar
                // pushNewScreen(context, screen: new ChatScreen());

                // This method moves the nav bar controller to the chat screen
                widget.handleTabSelection(chatPageIndex);
              },
              style: ButtonStyle(
                backgroundColor: MaterialStateProperty.all(secondaryThemeColor),
              ),
              child: const Text("Join Chat", style: TextStyle(fontWeight: FontWeight.bold),),
            ),
          ],
        ),
      ),
    );
  }
}
