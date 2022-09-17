import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

class AppointmentCard extends StatefulWidget {
  const AppointmentCard({Key? key, required this.name, required this.age,
    required this.startDateTime, required this.endDateTime}) : super(key: key);

  final String name;
  final int age;
  final DateTime startDateTime;
  final DateTime endDateTime;

  @override
  _AppointmentCardState createState() => _AppointmentCardState();
}

class _AppointmentCardState extends State<AppointmentCard>{
  @override
  Widget build(BuildContext context) {
    return Card(
      color: Colors.deepPurple[200],
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
                Container(
                  width: 100,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[
                      Text(
                        // if name exceeds length
                          widget.name.length > 13 ? "${widget.name.substring(0, 13)}..." : widget.name,
                          style: const TextStyle(
                            color: Colors.white,
                            fontWeight: FontWeight.bold,
                          )
                      ),
                      Text(
                          "Age: ${widget.age.toString()}",
                          style: const TextStyle(
                            color: Colors.white,
                          )
                      ),
                    ],
                  ),
                ),
                const SizedBox(width: 25),
                ElevatedButton(
                  onPressed: () {},
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.red,
                  ),
                  child: const Text(
                      "Cancel",
                      style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                      )
                  ),
                ),
                const SizedBox(width: 20),
                IconButton(
                  onPressed: () {},
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
            ),  // Replace with line
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
                  DateFormat('dd MMM yyyy').format(widget.startDateTime),
                  style: const TextStyle(
                    color: Colors.white,
                  ),
                ),
                const SizedBox(
                  width: 50,
                ),
                const Icon(
                  CupertinoIcons.time,
                  color: Colors.white,
                ),
                const SizedBox(
                  width: 10,
                ),
                Text(
                  "${DateFormat.jm().format(widget.startDateTime)} - ${DateFormat.jm().format(widget.endDateTime)}",
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