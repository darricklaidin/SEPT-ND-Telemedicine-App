import 'package:flutter/material.dart';

import 'package:frontend/services/auth_service.dart';

import '../../main.dart';
import '../doctor/edit_availabilities_screen.dart';
import 'edit_doctor_personal_details_screen.dart';
import 'edit_patient_health_status.dart';
import 'edit_patient_personal_details_screen.dart';

class SettingsScreen extends StatefulWidget {
  const SettingsScreen({Key? key}) : super(key: key);

  @override
  State<SettingsScreen> createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  dynamic user;
  String userRole = "NO ROLE";

  bool isLoading = true;

  loadUserData() async {
    setState(() {
      isLoading = true;
    });

    user = await AuthService.getUserFromStorage();
    userRole = await AuthService.getUserRoleFromStorage();

    setState(() {
      isLoading = false;
    });
  }

  @override
  void initState() {
    super.initState();
    loadUserData();
  }

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    Color primaryThemeColor = Theme.of(context).colorScheme.primary;
    Color secondaryThemeColor = Theme.of(context).colorScheme.secondary;

    return Scaffold(
      appBar: AppBar(
        title: const Text("Settings"),
      ),
      body: Builder(builder: (context) {
        if (isLoading) {
          return const Center(child: CircularProgressIndicator());
        } else {
          return Center(
            child: Padding(
              padding: const EdgeInsets.all(0),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  userRole == "PATIENT"
                      ? _buildEditHealthStatusBtn(context, width, height,
                          primaryThemeColor, secondaryThemeColor)
                      : _buildEditMyAvailabilitiesBtn(context, width, height,
                          primaryThemeColor, secondaryThemeColor),
                  SizedBox(height: height * 0.05),
                  ..._buildEditPersonalDetailsBtn(
                      context,
                      user.userID,
                      userRole,
                      width,
                      height,
                      primaryThemeColor,
                      secondaryThemeColor),
                  SizedBox(height: height * 0.05),
                  ..._buildNotificationSettingsBtn(context, width, height,
                      primaryThemeColor, secondaryThemeColor),
                  SizedBox(height: height * 0.1),
                  ..._buildLogoutBtn(context, width, height, primaryThemeColor,
                      secondaryThemeColor),
                ],
              ),
            ),
          );
        }
      }),
    );
  }
}

Widget _buildEditHealthStatusBtn(BuildContext context, double width,
    double height, Color primaryThemeColor, Color secondaryThemeColor) {
  return SizedBox(
    width: width * 0.5,
    height: height * 0.07,
    child: ElevatedButton(
      onPressed: () {
        Navigator.push(
            context,
            MaterialPageRoute(
                builder: (context) => const EditPatientHealthStatusScreen()));
      },
      style: ButtonStyle(
        shape: MaterialStateProperty.all<RoundedRectangleBorder>(
          RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(10.0),
          ),
        ),
      ),
      child: const Text(
        "Edit Health Status",
        style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
      ),
    ),
  );
}

Widget _buildEditMyAvailabilitiesBtn(BuildContext context, double width,
    double height, Color primaryThemeColor, Color secondaryThemeColor) {
  return SizedBox(
    width: width * 0.5,
    height: height * 0.07,
    child: ElevatedButton(
      onPressed: () {
        Navigator.push(
            context,
            MaterialPageRoute(
                builder: (context) => const EditAvailabilityScreen()));
      },
      style: ButtonStyle(
        shape: MaterialStateProperty.all<RoundedRectangleBorder>(
          RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(10.0),
          ),
        ),
      ),
      child: const Text(
        "Edit My Availabilities",
        style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
      ),
    ),
  );
}

List<Widget> _buildEditPersonalDetailsBtn(
    BuildContext context,
    int userID,
    String userRole,
    double width,
    double height,
    Color primaryThemeColor,
    Color secondaryThemeColor) {
  return [
    SizedBox(
      width: width * 0.5,
      height: height * 0.07,
      child: ElevatedButton(
        onPressed: () {
          if (userRole == "PATIENT") {
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => EditPatientPersonalDetailsScreen(
                          patientID: userID,
                        )));
          } else if (userRole == "DOCTOR") {
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => EditDoctorPersonalDetailsScreen(
                          doctorID: userID,
                        )));
          }
        },
        style: ButtonStyle(
          shape: MaterialStateProperty.all<RoundedRectangleBorder>(
            RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10.0),
            ),
          ),
        ),
        child: const Text(
          "Edit Personal Details",
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
      ),
    ),
  ];
}

List<Widget> _buildNotificationSettingsBtn(BuildContext context, double width,
    double height, Color primaryThemeColor, Color secondaryThemeColor) {
  return [
    SizedBox(
      width: width * 0.5,
      height: height * 0.07,
      child: ElevatedButton(
        onPressed: () {},
        style: ButtonStyle(
          shape: MaterialStateProperty.all<RoundedRectangleBorder>(
            RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10.0),
            ),
          ),
        ),
        child: const Text(
          "Notification Settings",
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
      ),
    ),
  ];
}

logout(context) async {
  await AuthService.logoutUser();
  Navigator.of(context, rootNavigator: true).pushAndRemoveUntil(
      MaterialPageRoute(builder: (context) => const MyApp()),
      (route) => route.isFirst);
}

List<Widget> _buildLogoutBtn(BuildContext context, double width, double height,
    Color primaryThemeColor, Color secondaryThemeColor) {
  return [
    SizedBox(
      width: width * 0.5,
      height: height * 0.07,
      child: ElevatedButton(
        onPressed: () => logout(context),
        style: ButtonStyle(
          backgroundColor:
              MaterialStateProperty.all<Color>(secondaryThemeColor),
          shape: MaterialStateProperty.all<RoundedRectangleBorder>(
            RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(10.0),
            ),
          ),
        ),
        child: const Text(
          "Logout",
          style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
        ),
      ),
    ),
  ];
}
