import 'dart:async';

import 'package:age_calculator/age_calculator.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:frontend/modules/profile/profile_button.dart';
import 'package:frontend/services/doctor_service.dart';
import 'package:frontend/utility.dart';

import '../../services/auth_service.dart';
import '../../services/patient_service.dart';
import '../../services/specialty_service.dart';
import '../appointment/book_appointment_screen.dart';
import '../prescription/create_prescription_screen.dart';

class ProfileScreen extends StatefulWidget {
  final AuthService authService;
  final DoctorService doctorService;
  final SpecialtyService specialtyService;

  final user;
  final userRole;

  const ProfileScreen({
    Key? key,
    required this.user,
    required this.userRole,
    required this.authService,
    required this.doctorService,
    required this.specialtyService,
  }) : super(key: key);

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  List availabilitySchedule = [];
  String healthStatus = "No health status to display.";
  bool isLoading = true;
  bool timeUp = false;

  Future loadAvailabilities() async {
    isLoading = true;
    timeUp = false;

    try {
      availabilitySchedule =
          await DoctorService.fetchDoctorAvailabilities(widget.user.userID);
    } on TimeoutException {
      setState(() {
        timeUp = true;
        isLoading = false;
      });
      return;
    } on Exception catch (exception) {
      print(exception);
    }

    setState(() {
      isLoading = false;
    });
  }

  Future checkIfUserExists(Color errorThemeColor) async {
    if (widget.userRole == "PATIENT"
        ? await DoctorService.fetchDoctor(widget.user.userID) ==
            "Resource Not Found"
        : await PatientService.fetchPatient(widget.user.userID) ==
            "Resource Not Found") {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: const EdgeInsets.only(bottom: 10.0),
          duration: const Duration(seconds: 2),
          content: const Text('User no longer exists'),
          backgroundColor: errorThemeColor,
        ),
      );

      Navigator.pop(context);
      return false;
    }

    return true;
  }

  @override
  void initState() {
    super.initState();
    if (widget.userRole == "PATIENT") {
      loadAvailabilities();
    } else if (widget.userRole == "DOCTOR") {
      setState(() {
        healthStatus = widget.user.symptoms ?? healthStatus;
        isLoading = false;
      });
    } else {
      setState(() {
        isLoading = false;
      });
    }
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
        backgroundColor: Colors.transparent,
        elevation: 0,
        title: _buildTitleRow(
            width, height, primaryThemeColor, secondaryThemeColor),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          color: Colors.black,
          onPressed: () {
            Navigator.pop(context);
          },
        ),
      ),
      body: Builder(
        builder: (context) {
          if (isLoading) {
            return const Center(child: CircularProgressIndicator());
          } else {
            return RefreshIndicator(
              onRefresh: () async {
                if (await checkIfUserExists(errorThemeColor)) {
                  if (widget.userRole == "PATIENT") {
                    await loadAvailabilities();
                  }
                }
              },
              child: SizedBox(
                height: height,
                child: SingleChildScrollView(
                  physics: const AlwaysScrollableScrollPhysics(),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      ..._buildProfileHeader(width, height, primaryThemeColor,
                          secondaryThemeColor),
                      SizedBox(height: height * 0.01),
                      ..._buildHealthStatus(width, height, primaryThemeColor,
                          secondaryThemeColor),
                      ..._buildAvailabilitySchedule(
                          width,
                          height,
                          primaryThemeColor,
                          secondaryThemeColor,
                          errorThemeColor),
                      SizedBox(height: height * 0.05),
                      ..._buildBookAppointmentBtn(
                          width,
                          height,
                          primaryThemeColor,
                          secondaryThemeColor,
                          errorThemeColor),
                      ..._buildPrescriptionBtn(width, height, primaryThemeColor,
                          secondaryThemeColor, errorThemeColor),
                    ],
                  ),
                ),
              ),
            );
          }
        },
      ),
    );
  }

  Row _buildTitleRow(double width, double height, Color primaryThemeColor,
      Color secondaryThemeColor) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.end,
      children: [
        ProfileButton(
          authService: widget.authService,
          specialtyService: widget.specialtyService,
        ),
        SizedBox(
          width: width * 0.05,
        )
      ],
    );
  }

  List<Widget> _buildProfileHeader(double width, double height,
      Color primaryThemeColor, Color secondaryThemeColor) {
    return [
      const Center(
        child: Icon(CupertinoIcons.profile_circled, size: 100),
      ),
      SizedBox(
        height: height * 0.05,
      ),
      Text(
        '${widget.user.firstName} ${widget.user.lastName}',
        style: const TextStyle(fontSize: 21, wordSpacing: 1),
        textAlign: TextAlign.center,
      ),
      Text(
        widget.userRole == "PATIENT"
            ? '${widget.user.specialty}'
            : 'Age: ${AgeCalculator.age(widget.user.dateOfBirth).years}',
        style: const TextStyle(fontSize: 14, color: Colors.grey),
        textAlign: TextAlign.center,
      ),
    ];
  }

  List<Widget> _buildHealthStatus(double width, double height,
      Color primaryThemeColor, Color secondaryThemeColor) {
    if (widget.userRole == "PATIENT") return [SizedBox(height: height * 0.05)];
    return [
      SizedBox(
        height: height * 0.05,
      ),
      const Text(
        'Health Status',
        style: TextStyle(fontSize: 21),
        textAlign: TextAlign.center,
      ),
      SizedBox(
        height: height * 0.05,
      ),
      Text(
        healthStatus,
        style: const TextStyle(fontSize: 14, color: Colors.grey),
        textAlign: TextAlign.center,
      ),
    ];
  }

  List<Widget> _buildAvailabilitySchedule(
      double width,
      double height,
      Color primaryThemeColor,
      Color secondaryThemeColor,
      Color errorThemeColor) {
    if (widget.userRole == "PATIENT") {
      if (timeUp) {
        return [
          SizedBox(
            child: Container(
              padding: EdgeInsets.fromLTRB(0, height * 0.05, 0, 0),
              child: const Text(
                  "Timeout: Unable to fetch the doctor's availabilities"),
            ),
          ),
        ];
      } else if (availabilitySchedule.isEmpty) {
        return [
          SizedBox(
            child: Container(
              padding: EdgeInsets.fromLTRB(0, height * 0.05, 0, 0),
              child: const Text("No availabilities found"),
            ),
          ),
        ];
      }

      return [
        ListView.separated(
          itemCount: availabilitySchedule.length,
          itemBuilder: (context, index) {
            return Container(
              padding: const EdgeInsets.symmetric(horizontal: 0, vertical: 0),
              height: height * 0.1,
              child: ListTile(
                contentPadding:
                    EdgeInsets.symmetric(horizontal: width * 0.1, vertical: 0),
                title: Text(
                  Utility.convertIntToDayOfWeek(
                      availabilitySchedule[index].dayOfWeek),
                  style: const TextStyle(fontSize: 14),
                ),
                subtitle: Text(
                    "${Utility.timeToString(availabilitySchedule[index].startTime)} "
                    "- ${Utility.timeToString(availabilitySchedule[index].endTime)}",
                    style: const TextStyle(fontSize: 12)),
              ),
            );
          },
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          separatorBuilder: (context, index) {
            return const SizedBox(height: 0);
          },
        ),
      ];
    }
    return [SizedBox(height: height * 0.05)];
  }

  List<Widget> _buildBookAppointmentBtn(
      double width,
      double height,
      Color primaryThemeColor,
      Color secondaryThemeColor,
      Color errorThemeColor) {
    return widget.userRole == "DOCTOR"
        ? [SizedBox()]
        : [
            Center(
              child: SizedBox(
                width: width * 0.45,
                height: 40,
                child: TextButton(
                  // Navigate to make appointment page
                  onPressed: () async {
                    if (await checkIfUserExists(errorThemeColor)) {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => BookAppointmentScreen(
                            doctor: widget.user,
                            authService: widget.authService,
                            doctorService: widget.doctorService,
                          ),
                        ),
                      );
                    }
                    await loadAvailabilities();
                  },
                  style: ButtonStyle(
                    backgroundColor:
                        MaterialStateProperty.all(secondaryThemeColor),
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

  List<Widget> _buildPrescriptionBtn(
      double width,
      double height,
      Color primaryThemeColor,
      Color secondaryThemeColor,
      Color errorThemeColor) {
    return widget.userRole == "PATIENT"
        ? [
            SizedBox(
              height: height * 0.05,
            )
          ]
        : [
            Center(
              child: SizedBox(
                width: width * 0.45,
                height: 40,
                child: TextButton(
                  // Navigate to make appointment page
                  onPressed: () async {
                    if (await checkIfUserExists(errorThemeColor)) {
                      Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (context) => PrescriptionScreen(
                                    patient: widget.user,
                                    authService: widget.authService,
                                  )));
                    }
                  },
                  style: ButtonStyle(
                    backgroundColor:
                        MaterialStateProperty.all(secondaryThemeColor),
                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                      RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8.0),
                      ),
                    ),
                  ),
                  child: const Text(
                    'Prescribe Medicine',
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
