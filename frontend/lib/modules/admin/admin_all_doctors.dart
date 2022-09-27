import 'package:age_calculator/age_calculator.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:frontend/models/doctor.dart';
import 'package:frontend/services/appointment_service.dart';
import 'package:frontend/services/doctor_service.dart';
import 'package:frontend/utility.dart';
import 'package:frontend/modules/appointment/appointment_card.dart';
import 'package:frontend/models/appointment.dart';
import 'package:intl/intl.dart';

class AdminAllDoctorsScreen extends StatefulWidget {
  AdminAllDoctorsScreen({Key? key}) : super(key: key);

  @override
  _AdminAllDoctorsScreen createState() =>
      _AdminAllDoctorsScreen();
}

class _AdminAllDoctorsScreen extends State<AdminAllDoctorsScreen> {

  List<Doctor> doctors = List<Doctor>.empty(growable: true);
  bool isLoading = true;

  void loadDoctors() async {
    doctors = await DoctorService.fetchDoctorList();
    setState(() {
      doctors = doctors;
      isLoading = false;
    });
  }

  @override
  void initState() {
    super.initState();
    loadDoctors();
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
                        itemCount: patients.length,
                        itemBuilder: (context, index) {
                          return Container(
                            height: 40,
                            child: Center(child:Text('${patients[index]}')),
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