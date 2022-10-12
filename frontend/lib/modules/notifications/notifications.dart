import 'package:flutter/material.dart';

class NotificationScreen extends StatefulWidget {
  const NotificationScreen({Key? key}) : super(key: key);

  @override
  State<NotificationScreen> createState() => _NotificationScreenState();
}

class _NotificationScreenState extends State<NotificationScreen> {
  @override
  Widget build(BuildContext context) {
    Color primaryThemeColor = Theme.of(context).colorScheme.primary;

    return SafeArea(
      child: Scaffold(
        body: Padding(
          padding: EdgeInsets.fromLTRB(
              (MediaQuery.of(context).size.height * 0.03),
              (MediaQuery.of(context).size.height * 0.05),
              (MediaQuery.of(context).size.height * 0.03),
              0),
          child: SingleChildScrollView(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    const Icon(Icons.notifications),
                    SizedBox(width: (MediaQuery.of(context).size.width * 0.01)),
                    const Text(
                      "Notifications",
                      style: TextStyle(
                          color: Colors.black,
                          fontSize: 22,
                          fontWeight: FontWeight.bold),
                    ),
                  ],
                ),
                SizedBox(height: (MediaQuery.of(context).size.height * 0.02)),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text(
                      "Today",
                      style: TextStyle(
                          color: Colors.black,
                          fontSize: 20,
                          fontWeight: FontWeight.normal),
                    ),
                    Expanded(
                      child: Container(
                        margin: const EdgeInsets.only(left: 10.0, right: 20.0),
                        child: const Divider(
                          color: Colors.black,
                          height: 36,
                        ),
                      ),
                    ),
                  ],
                ),
                SingleChildScrollView(
                    child: Center(
                  child: SizedBox(
                    width: (MediaQuery.of(context).size.width * 0.87),
                    child: ListView.builder(
                      itemCount: 5,
                      shrinkWrap: true,
                      itemBuilder: (BuildContext context, int index) {
                        return Column(
                          children: [
                            Card(
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(10.0),
                              ),
                              color: primaryThemeColor,
                              child: Padding(
                                padding:
                                    const EdgeInsets.fromLTRB(5, 10, 10, 5),
                                child: Row(
                                  children: [
                                    SizedBox(
                                        width:
                                            (MediaQuery.of(context).size.width *
                                                0.05)),
                                    CircleAvatar(
                                      radius: 25,
                                      child: Image.asset('assets/patient.png'),
                                    ),
                                    SizedBox(
                                        width:
                                            (MediaQuery.of(context).size.width *
                                                0.03)),
                                    Column(
                                      children: [
                                        const Text(
                                            "Time to take your medication",
                                            style: TextStyle(
                                                color: Colors.white,
                                                fontWeight: FontWeight.bold)),
                                        SizedBox(
                                            height: (MediaQuery.of(context)
                                                    .size
                                                    .height *
                                                0.01)),
                                        const Text("Panadol (6 pills) - 6AM ",
                                            style: TextStyle(
                                              color: Colors.white,
                                            )),
                                        SizedBox(
                                            height: (MediaQuery.of(context)
                                                    .size
                                                    .height *
                                                0.01)),
                                      ],
                                    )
                                  ],
                                ),
                              ),
                            ),
                            SizedBox(
                                height: (MediaQuery.of(context).size.height *
                                    0.02)),
                          ],
                        );
                      },
                    ),
                  ),
                )),
                SizedBox(height: (MediaQuery.of(context).size.height * 0.025)),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    const Text(
                      "Yesterday",
                      style: TextStyle(
                          color: Colors.black,
                          fontSize: 20,
                          fontWeight: FontWeight.normal),
                    ),
                    Expanded(
                      child: Container(
                        margin: const EdgeInsets.only(left: 10.0, right: 20.0),
                        child: const Divider(
                          color: Colors.black,
                          height: 36,
                        ),
                      ),
                    ),
                  ],
                ),
                SingleChildScrollView(
                  child: Center(
                    child: SizedBox(
                      width: (MediaQuery.of(context).size.width * 0.87),
                      child: ListView.builder(
                        itemCount: 4,
                        shrinkWrap: true,
                        itemBuilder: (BuildContext context, int index) {
                          return Column(
                            children: [
                              Card(
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(10.0),
                                ),
                                color: primaryThemeColor,
                                child: Padding(
                                  padding:
                                      const EdgeInsets.fromLTRB(5, 10, 10, 5),
                                  child: Row(
                                    children: [
                                      SizedBox(
                                          width: (MediaQuery.of(context)
                                                  .size
                                                  .width *
                                              0.05)),
                                      CircleAvatar(
                                        radius: 25,
                                        child:
                                            Image.asset('assets/patient.png'),
                                      ),
                                      SizedBox(
                                          width: (MediaQuery.of(context)
                                                  .size
                                                  .width *
                                              0.03)),
                                      Column(
                                        children: [
                                          const Text(
                                              "Time to take your medication",
                                              style: TextStyle(
                                                  color: Colors.white,
                                                  fontWeight: FontWeight.bold)),
                                          SizedBox(
                                              height: (MediaQuery.of(context)
                                                      .size
                                                      .height *
                                                  0.01)),
                                          const Text("Panadol (6 pills) - 6AM ",
                                              style: TextStyle(
                                                color: Colors.white,
                                              )),
                                          SizedBox(
                                              height: (MediaQuery.of(context)
                                                      .size
                                                      .height *
                                                  0.01)),
                                        ],
                                      )
                                    ],
                                  ),
                                ),
                              ),
                              SizedBox(
                                height:
                                    (MediaQuery.of(context).size.height * 0.02),
                              ),
                            ],
                          );
                        },
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
