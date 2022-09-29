import 'package:age_calculator/age_calculator.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:frontend/modules/profile/profile_button.dart';

class ProfileScreen extends StatefulWidget {

  final user;
  final userRole;

  const ProfileScreen({Key? key, required this.user, required this.userRole}) : super(key: key);

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    Color primaryThemeColor = Theme.of(context).colorScheme.primary;
    Color secondaryThemeColor = Theme.of(context).colorScheme.secondary;

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        title: _buildTitleRow(),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          color: Colors.black,
          onPressed: () {
            Navigator.pop(context);
          },
        ),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          ..._buildProfileHeader(width, height, primaryThemeColor, secondaryThemeColor),
          SizedBox(height: height * 0.05),
          ..._buildBookAppointmentBtn(width, height, primaryThemeColor, secondaryThemeColor),
        ],
      ),
    );
  }

  Row _buildTitleRow() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.end,
      children: const [
        ProfileButton(),
        SizedBox(
          width: 20,
        )
      ],
    );
  }

  List<Widget> _buildProfileHeader(double width, double height, Color primaryThemeColor, Color secondaryThemeColor) {
    return [
      const Center(
        child: Icon(CupertinoIcons.profile_circled, size: 100),
      ),
      const SizedBox(
        height: 20,
      ),
      Text(
        '${widget.user.firstName} ${widget.user.lastName}',
        style: const TextStyle(fontSize: 21, wordSpacing: 1),
        textAlign: TextAlign.center,
      ),
      Text(
        widget.userRole == "PATIENT" ? '${widget.user.specialty}' : 'Age: ${AgeCalculator.age(widget.user.dateOfBirth).years}',
        style: const TextStyle(fontSize: 18, color: Colors.grey),
        textAlign: TextAlign.center,
      ),
      const SizedBox(
        height: 10,
      )
    ];
  }

  List<Widget> _buildBookAppointmentBtn(double width, double height, Color primaryThemeColor, Color secondaryThemeColor) {
    return widget.userRole == "DOCTOR" ? [SizedBox(height: height * 0.05,)] : [
      Center(
        child: SizedBox(
          width: width * 0.45,
          height: 40,
          child: TextButton(
            // TODO: Navigate to make appointment page
            onPressed: () {

            },
            style: ButtonStyle(
              backgroundColor: MaterialStateProperty.all(secondaryThemeColor),
              shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8.0),
                ),
              ),
            ),
            child: const Text(
              'Book Appointment',
              style: TextStyle(
                color: Colors.white,
                fontSize: 14,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
        ),
      ),
      SizedBox(
        height: height * 0.05,
      )
    ];
  }
}