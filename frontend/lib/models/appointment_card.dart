import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class AppointmentCard extends StatefulWidget {
  final String name;
  final int age;
  final String date;
  final String startTime;
  final String endTime;

  final Function delete;

  const AppointmentCard({Key? key, required this.name, required this.age,
    required this.date, required this.startTime, required this.endTime, required this.delete}) : super(key: key);

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
                  onPressed: () {
                    widget.delete();
                  },
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