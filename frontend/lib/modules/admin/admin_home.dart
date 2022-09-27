import 'package:flutter/material.dart';

class AdminHomeScreen extends StatelessWidget {
  const AdminHomeScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.red,
        elevation: 0,
        title: const Center(child: Text("Admin Home")),
      ),

      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 40.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            const Padding(
              padding: EdgeInsets.all(10.0),
              child: Center(child: Text(
                "Welcome Admin!",
                style: TextStyle(
                  fontSize: 26,
                  fontWeight: FontWeight.bold,
                ),
              )),
            ),
            ..._ViewAllDoctorsBtn(),
            ..._buildNewDoctorBtn()
          ],
        ),
      ),

    );
  }
}

  List<Widget> _buildNewDoctorBtn() {
    return [
      Padding(
        padding: const EdgeInsets.all(40.0),
        child:
          SizedBox(
            height: 40,
            child: TextButton(
              onPressed: () {},
              style: ButtonStyle(
                backgroundColor: MaterialStateProperty.all(
                    Colors.blueAccent,
                ),
                padding: MaterialStateProperty.all(
                  const EdgeInsets.symmetric(horizontal: 65, vertical: 0),
                ),
                shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                  RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(8.0),
                  ),
                ),
              ),
              child: const Text(
                'Add New Doctor',
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 14,
                ),
              ),
            ),
          ),
        ),
    ];
  }

List<Widget> _ViewAllDoctorsBtn() {
  return [
    Padding(
      padding: const EdgeInsets.all(40.0),
      child: Center(
        child: SizedBox(
          height: 40,
          child: TextButton(
            onPressed: () {},
            style: ButtonStyle(
              backgroundColor: MaterialStateProperty.all(
                Colors.blueAccent,
              ),
              padding: MaterialStateProperty.all(
                const EdgeInsets.symmetric(horizontal: 65, vertical: 0),
              ),
              shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8.0),
                ),
              ),
            ),
            child: const Text(
              'View All Doctors',
              style: TextStyle(
                color: Colors.white,
                fontSize: 14,
              ),
            ),
          ),
        ),
      ),
    ),
    const Spacer(
      flex: 3,
    ),
  ];
}
