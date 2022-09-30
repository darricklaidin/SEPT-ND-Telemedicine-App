import 'dart:async';

import 'package:age_calculator/age_calculator.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:frontend/services/doctor_service.dart';
import 'package:intl/intl.dart';

import 'package:frontend/services/appointment_service.dart';
import 'package:frontend/services/patient_service.dart';
import 'package:frontend/utility.dart';
import 'package:frontend/modules/appointment/appointment_card.dart';
import 'package:frontend/models/appointment.dart';
import 'package:frontend/services/auth_service.dart';

import '../profile/profile_button.dart';


class ManageAppointmentsScreen extends StatefulWidget {
  final Function handleTabSelection;

  ManageAppointmentsScreen({Key? key, required this.handleTabSelection,}) : super(key: key);

  @override
  _ManageAppointmentsScreenState createState() =>
      _ManageAppointmentsScreenState();
}

class _ManageAppointmentsScreenState extends State<ManageAppointmentsScreen> {
  List<Appointment> appointments = List<Appointment>.empty(growable: true);
  bool isLoading = true;
  bool timeUp = false;
  String? userRole;

  Future loadAppointments() async {
    isLoading = true;
    timeUp = false;
    userRole = await getUserRoleFromStorage();

    try {
      // If role is patient, then fetch patient appointments
      if (userRole == "PATIENT") {
        appointments = await PatientService.fetchPatientAppointments(await getUserIdFromStorage());
      }
      // If role is doctor, then fetch doctor appointments
      else if (userRole == "DOCTOR") {
        appointments = await DoctorService.fetchDoctorAppointments(await getUserIdFromStorage());
      }
    } on TimeoutException {
      setState(() {
        timeUp = true;
        isLoading = false;
      });
      return;
    } on Exception catch (exception) {
      print(exception);
      return;
    }

    setState(() {
      isLoading = false;
    });
  }

  @override
  void initState() {
    super.initState();
    loadAppointments();
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
        title: Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: const [
            ProfileButton(),
            SizedBox(
              width: 20,
            )
          ],
        ),
        automaticallyImplyLeading: false,
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(vertical: 0, horizontal: 20),
        child: Column(
          children: <Widget>[
            const Align(
              alignment: Alignment.centerLeft,
              child: Text("Appointments",
                style: TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            const SizedBox(height: 25),
            Builder(builder: (context) {
              if (isLoading) {
                return const Padding(
                  padding: EdgeInsets.fromLTRB(0, 100, 0, 0),
                  child: Center(
                    child: CircularProgressIndicator(),
                  ),
                );
              } else if (timeUp) {
                return Expanded(
                  child: RefreshIndicator(
                    onRefresh: () async {
                      await loadAppointments();
                    },
                    child: ListView(
                      children: const [
                        Center(
                          child: Text("Timeout: Unable to fetch appointments"),
                        ),
                      ]
                    ),
                  ),
                );
              } else if (appointments.isEmpty) {
                return Expanded(
                  child: RefreshIndicator(
                    onRefresh: () async {
                      await loadAppointments();
                    },
                    child: ListView(
                        children: const [
                          Center(
                            child: Text("No appointments found"),
                          ),
                        ]
                    ),
                  ),
                );
              } else {
                  return Expanded(
                    child: RefreshIndicator(
                      onRefresh: () async {
                        await loadAppointments();
                      },
                      child: ListView.builder(
                          padding: const EdgeInsets.all(0),
                          itemCount: appointments.length,
                          itemBuilder: (context, index) {
                            // Display appropriate info based on role
                            return AppointmentCard(
                              appointmentID: appointments[index].appointmentID,
                              name:
                              userRole == "PATIENT" ?
                              "${appointments[index].doctor.firstName} "
                                  "${appointments[index].doctor.lastName}" :
                              "${appointments[index].patient.firstName} "
                                  "${appointments[index].patient.lastName}",

                              age: AgeCalculator.age(
                                      userRole == "PATIENT" ?
                                      appointments[index].doctor.dateOfBirth :
                                      appointments[index].patient.dateOfBirth).years,

                              date: DateFormat('dd MMM yyyy')
                                  .format(appointments[index].date),
                              startTime:
                                  Utility.timeToString(appointments[index].startTime),
                              endTime:
                                  Utility.timeToString(appointments[index].endTime),
                              delete: () async {
                                await AppointmentService.deleteAppointment(
                                    appointments[index].appointmentID);
                                setState(() {
                                  appointments.removeAt(index);
                                });
                                if (!mounted) {
                                  return;
                                }
                                ScaffoldMessenger.of(context).showSnackBar(
                                  SnackBar(
                                    behavior: SnackBarBehavior.floating,
                                    margin: const EdgeInsets.only(bottom: 10.0),
                                    content: const Text("Appointment deleted"),
                                    duration: const Duration(seconds: 2),
                                    backgroundColor: errorThemeColor,
                                  ),
                                );
                              },
                              handleTabSelection: widget.handleTabSelection,
                              reload: loadAppointments,
                            );
                          }),
                    ),
                  );
              }
            }),
        ],
      ),
    ));
  }
}
