import 'dart:async';

import 'package:flutter/material.dart';
import 'package:frontend/modules/profile/profile_button.dart';

import '../../models/patient.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:frontend/models/prescription.dart';
import 'package:frontend/services/prescription_service.dart';

class PatientViewPrescriptionScreen extends StatefulWidget {
  final AuthService authService;
  const PatientViewPrescriptionScreen({Key? key, required this.patient, required this.authService})
      : super(key: key);

  final Patient patient;

  @override
  State<PatientViewPrescriptionScreen> createState() => PatientViewPrescriptionScreenState();
}

class PatientViewPrescriptionScreenState extends State<PatientViewPrescriptionScreen> {
  List<Prescription> prescriptions = List<Prescription>.empty(growable: true);
  bool isLoading = true;
  bool timeUp = false;

  Future loadPrescriptions() async {
    isLoading = true;
    timeUp = false;

    try {
      prescriptions = await PrescriptionService
          .fetchPatientPrescriptions(await AuthService.getUserIdFromStorage());
    } on TimeoutException {
      if (!mounted) return;

      setState(() {
        timeUp = true;
        isLoading = false;
      });
      return;
    }

    setState(() {
      isLoading = false;
    });
  }

  @override
  void initState() {
    super.initState();
    loadPrescriptions();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        title: Row(
          mainAxisAlignment: MainAxisAlignment.end,
          children: [
            ProfileButton(authService: widget.authService),
            const SizedBox(
              width: 20,
            ),
          ],
        ),
        automaticallyImplyLeading: false,
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(vertical: 0, horizontal: 20),
        child: Column(
          children: [
            const Align(
              alignment: Alignment.centerLeft,
              child: Text(
                'Prescriptions',
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
                      await loadPrescriptions();
                    },
                    child: ListView(
                      children: const [
                        Center(
                          child: Text("Timeout: Unable to fetch appointments"),
                        )
                      ],),
                  ),
                );
              } else if (prescriptions.isEmpty) {
                return Expanded(
                  child: RefreshIndicator(
                    onRefresh: () async {
                      await loadPrescriptions();
                    },
                    child: ListView(
                      children: const [
                        Center(
                          child: Text("No prescriptions found"),
                        )
                      ],),
                  ),
                );
              } else {

                return Expanded(
                  child: RefreshIndicator(
                    onRefresh: () async {
                      await loadPrescriptions();
                    },
                    child: ListView.builder(
                      padding: const EdgeInsets.all(0),
                      itemCount: prescriptions.length,
                      itemBuilder: (context, index) {
                        return Card(
                          child: ListTile(
                            title: Text(prescriptions[index].prescription),
                          ),
                        );
                      },
                    )
                  )
                );
              }
            })
          ],
        )
      ),
    );
  }




}