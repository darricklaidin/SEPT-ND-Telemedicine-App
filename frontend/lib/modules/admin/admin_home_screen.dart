import 'package:flutter/material.dart';

import '../../config/themes/light_palette.dart';
import '../../main.dart';
import '../../services/auth_service.dart';
import 'admin_create_new_doctor_screen.dart';

class AdminHomeScreen extends StatefulWidget {
  final AuthService authService;

  const AdminHomeScreen({Key? key, required this.authService})
      : super(key: key);

  @override
  State<AdminHomeScreen> createState() => _AdminHomeScreenState();
}

class _AdminHomeScreenState extends State<AdminHomeScreen> {
  logout(context) async {
    await widget.authService.logoutUser();
    Navigator.of(context, rootNavigator: true).pushAndRemoveUntil(
        MaterialPageRoute(builder: (context) => const MyApp()),
        (route) => route.isFirst);
  }

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Home'),
      ),
      body: Container(
        width: width,
        height: height,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            const Text(
              'Welcome Admin',
              style: TextStyle(fontWeight: FontWeight.bold),
            ),
            SizedBox(
              height: height * 0.05,
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => AdminCreateNewDoctorScreen(
                            authService: widget.authService)));
              },
              style: ButtonStyle(
                backgroundColor:
                    MaterialStateProperty.all(LightPalette.primary),
              ),
              child: const Text(
                'Add New Doctor',
                style: TextStyle(fontWeight: FontWeight.bold),
              ),
            ),
            SizedBox(
              height: height * 0.01,
            ),
            ElevatedButton(
              onPressed: () async {
                await logout(context);
              },
              style: ButtonStyle(
                backgroundColor:
                    MaterialStateProperty.all(LightPalette.secondary),
              ),
              child: const Text(
                'Logout',
                style: TextStyle(fontWeight: FontWeight.bold),
              ),
            )
          ],
        ),
      ),
    );
  }
}
