import 'dart:async';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:frontend/config/themes/light_palette.dart';
import 'package:frontend/services/doctor_service.dart';
import 'package:frontend/services/specialty_service.dart';
import 'package:frontend/utility.dart';
import '../../models/doctor.dart';
import '../../models/patient.dart';
import '../../services/patient_service.dart';

class AdminViewUserProfileScreen extends StatefulWidget {

  final user;
  final userRole;

  const AdminViewUserProfileScreen({Key? key, required this.user, required this.userRole}) : super(key: key);

  @override
  State<AdminViewUserProfileScreen> createState() => _AdminViewUserProfileScreenState();
}

class _AdminViewUserProfileScreenState extends State<AdminViewUserProfileScreen> {

  List availabilitySchedule = [];
  bool isLoading = true;
  bool timeUp = false;

  Future loadAvailabilities() async {
    isLoading = true;
    timeUp = false;

    try {
      availabilitySchedule = await DoctorService
          .fetchDoctorAvailabilities(widget.user.userID);
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
    if (widget.userRole == "DOCTOR" ?
    await DoctorService.fetchDoctor(widget.user.userID) == "Resource Not Found"
        : await PatientService.fetchPatient(widget.user.userID) == "Resource Not Found") {

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

  Future activateAccount() async {
    if (widget.userRole == "PATIENT") {
      Patient updatedPatient = await widget.user;

      setState(() {
        updatedPatient.accountStatus = true;
      });

      // set this user's account status to true by using updatePatient()
      await PatientService.updatePatient(widget.user.userID, updatedPatient, null);

      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          duration: Duration(seconds: 2),
          content: Text('Successfully activated account'),
          backgroundColor: LightPalette.success,
        ),
      );
    } else if (widget.userRole == "DOCTOR") {
      Doctor updatedDoctor = await widget.user;

      setState(() {
        updatedDoctor.accountStatus = true;
      });

      List specialties = await SpecialtyService.getSpecialties();

      int specialtyID = -1;
      for (var specialty in specialties) {
        if (specialty.specialtyName == updatedDoctor.specialty) {
          specialtyID = specialty.specialtyID;
        }
      }

      // set this user's account status to false by using updateDoctor()
      await DoctorService.updateDoctor(widget.user.userID, updatedDoctor, null, specialtyID);
      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          duration: Duration(seconds: 2),
          content: Text('Successfully activated account'),
          backgroundColor: LightPalette.success,
        ),
      );
    }
  }

  Future deactivateAccount() async {
    if (widget.userRole == "PATIENT") {
      Patient updatedPatient = await widget.user;

      setState(() {
        updatedPatient.accountStatus = false;
      });

      // set this user's account status to false by using updatePatient()
      await PatientService.updatePatient(widget.user.userID, updatedPatient, null);
      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          duration: Duration(seconds: 2),
          content: Text('Successfully deactivated account'),
          backgroundColor: LightPalette.success,
        ),
      );
    } else if (widget.userRole == "DOCTOR") {
      Doctor updatedDoctor = await widget.user;

      setState(() {
        updatedDoctor.accountStatus = false;
      });

      List specialties = await SpecialtyService.getSpecialties();

      int specialtyID = -1;
      for (var specialty in specialties) {
        if (specialty.specialtyName == updatedDoctor.specialty) {
          specialtyID = specialty.specialtyID;
        }
      }

      // set this user's account status to false by using updateDoctor()
      await DoctorService.updateDoctor(widget.user.userID, updatedDoctor, null, specialtyID);
      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          duration: Duration(seconds: 2),
          content: Text('Successfully deactivated account'),
          backgroundColor: LightPalette.success,
        ),
      );
    }
  }

  @override
  void initState() {
    super.initState();
    if (widget.userRole == "DOCTOR") {
      loadAvailabilities();
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
                  if (widget.userRole == "DOCTOR") {
                    loadAvailabilities();
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
                      ..._buildProfileHeader(width, height, primaryThemeColor, secondaryThemeColor),
                      SizedBox(height: height * 0.01),
                      ..._buildAvailabilitySchedule(width, height, primaryThemeColor, secondaryThemeColor, errorThemeColor),
                      SizedBox(height: height * 0.01),
                      ..._buildActivateAccountButton(width, height, primaryThemeColor, secondaryThemeColor, errorThemeColor),
                      SizedBox(height: height * 0.01),
                      ..._buildDeactivateAccountButton(width, height, primaryThemeColor, secondaryThemeColor, errorThemeColor),
                      SizedBox(height: height * 0.1),
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

  List<Widget> _buildProfileHeader(double width, double height, Color primaryThemeColor, Color secondaryThemeColor) {
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
        "Account Status: ${widget.user.accountStatus ? 'Active' : 'Inactive'}",
        style: const TextStyle(fontSize: 14, color: Colors.grey),
        textAlign: TextAlign.center,
      ),
    ];
  }

  List<Widget> _buildAvailabilitySchedule(double width, double height, Color primaryThemeColor, Color secondaryThemeColor, Color errorThemeColor) {
    if (widget.userRole == "DOCTOR") {
      if (timeUp) {
        return [
          SizedBox(
            child: Container(
              padding: EdgeInsets.fromLTRB(0, height * 0.05, 0, 0),
              child: const Text("Timeout: Unable to fetch the doctor's availabilities"),
            ),
          ),
        ];
      }
      else if (availabilitySchedule.isEmpty) {
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
                contentPadding: EdgeInsets.symmetric(horizontal: width * 0.1, vertical: 0),
                title: Text(Utility.convertIntToDayOfWeek(availabilitySchedule[index].dayOfWeek), style: const TextStyle(fontSize: 14),),
                subtitle: Text("${Utility.timeToString(availabilitySchedule[index].startTime)} "
                    "- ${Utility.timeToString(availabilitySchedule[index].endTime)}", style: const TextStyle(fontSize: 12)),
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

  List<Widget> _buildActivateAccountButton(double width, double height, Color primaryThemeColor, Color secondaryThemeColor, Color errorThemeColor) {
    return [
      ElevatedButton(
        onPressed: () {
          activateAccount();
        },
        style: ButtonStyle(
          backgroundColor: MaterialStateProperty.all<Color>(secondaryThemeColor),
        ),
        child: const Text("Activate Account",
          style: TextStyle(fontWeight: FontWeight.bold),),
      ),
    ];
  }

  List<Widget> _buildDeactivateAccountButton(double width, double height, Color primaryThemeColor, Color secondaryThemeColor, Color errorThemeColor) {
    return [
      ElevatedButton(
        onPressed: () {
          deactivateAccount();
        },
        style: ButtonStyle(
          backgroundColor: MaterialStateProperty.all<Color>(errorThemeColor),
        ),
        child: const Text("Deactivate Account",
          style: TextStyle(fontWeight: FontWeight.bold),),
      ),
    ];
  }

}