import 'package:age_calculator/age_calculator.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:frontend/services/appointment_service.dart';
import 'package:frontend/services/patient_service.dart';
import 'package:frontend/utility.dart';
import 'package:frontend/modules/appointment/appointment_card.dart';
import 'package:frontend/models/appointment.dart';
import 'package:intl/intl.dart';

class ManageAppointmentsScreen extends StatefulWidget {
  ManageAppointmentsScreen({Key? key}) : super(key: key);

  @override
  _ManageAppointmentsScreenState createState() =>
      _ManageAppointmentsScreenState();
}

class _ManageAppointmentsScreenState extends State<ManageAppointmentsScreen> {
  List<Appointment> appointments = List<Appointment>.empty(growable: true);
  bool isLoading = true;

  void loadAppointments() async {
    appointments = await PatientService.fetchPatientAppointments();
    setState(() {
      appointments = appointments;
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
    return Scaffold(
        body: Padding(
      padding: const EdgeInsets.symmetric(vertical: 0, horizontal: 20),
      child: Column(
        children: <Widget>[
          Align(
              alignment: Alignment.centerRight,
              child: IconButton(
                splashRadius: 20.0,
                iconSize: 35.0,
                icon: const Icon(CupertinoIcons.profile_circled),
                onPressed: () {},
              )),
          const Align(
            alignment: Alignment.centerLeft,
            child: Text(
              "Appointments",
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
            } else {
              return Expanded(
                child: ListView.builder(
                    padding: const EdgeInsets.all(0),
                    itemCount: appointments.length,
                    itemBuilder: (context, index) {
                      return AppointmentCard(
                        name:
                            "${appointments[index].doctor.firstName} ${appointments[index].doctor.lastName}",
                        age: AgeCalculator.age(
                                appointments[index].doctor.dateOfBirth)
                            .years,
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
                            const SnackBar(
                              behavior: SnackBarBehavior.floating,
                              margin: EdgeInsets.only(bottom: 10.0),
                              content: Text("Appointment deleted"),
                              duration: Duration(seconds: 2),
                            ),
                          );
                        },
                      );
                    }),
              );
            }
          }),
        ],
      ),
    ));
  }
}
