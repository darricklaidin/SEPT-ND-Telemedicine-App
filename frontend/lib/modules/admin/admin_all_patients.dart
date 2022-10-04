import 'package:age_calculator/age_calculator.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:frontend/models/patient.dart';
import 'package:frontend/services/appointment_service.dart';
import 'package:frontend/services/patient_service.dart';
import 'package:frontend/utility.dart';
import 'package:frontend/modules/appointment/appointment_card.dart';
import 'package:frontend/models/appointment.dart';
import 'package:intl/intl.dart';

class AdminAllPatientsScreen extends StatefulWidget {
  AdminAllPatientsScreen({Key? key}) : super(key: key);

  @override
  _AdminAllPatientsScreen createState() =>
      _AdminAllPatientsScreen();
}

class _AdminAllPatientsScreen extends State<AdminAllPatientsScreen> {

  List<Patient> patients = List<Patient>.empty(growable: true);
  bool isLoading = true;

  void loadPatients() async {
    patients = await PatientService.fetchPatientList();
    setState(() {
      patients = patients;
      isLoading = false;
    });
  }

  @override
  void initState() {
    super.initState();
    loadPatients();
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
                if (false) {
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
                        itemCount: patients.length,
                        itemBuilder: (context, index) {
                          return ListTile(
                            title: Text("${patients[index].firstName}"),
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