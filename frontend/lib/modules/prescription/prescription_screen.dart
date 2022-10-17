import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:persistent_bottom_nav_bar/persistent-tab-view.dart';

import '../../models/patient.dart';
import '../../services/doctor_service.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:frontend/models/prescription.dart';
import '../../models/api_response.dart';
import '../../config/themes/light_palette.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:frontend/services/prescription_service.dart';

import '../../models/doctor.dart';

 

class PrescriptionScreen extends StatefulWidget {
  final Patient patient;
  const PrescriptionScreen({Key? key, required this.patient}) : super(key: key);
  
  @override
  State<PrescriptionScreen> createState() => _PrescriptionScreenState();
}

class _PrescriptionScreenState extends State<PrescriptionScreen> {
  List<Prescription> prescriptions = List<Prescription>.empty(growable: true);
  bool isLoading = true;

  final textarea = TextEditingController();
  String prescriptionText = "";

  Future createPrescription() async {
    // Create prescription
    // prescriptionText would have the text value in the text box

    // FIXME: Replace hard coded user IDs with actual IDs
    int doctorID = await getUserIdFromStorage();  // access from getuserIDfromstorage
    int patientID = widget.patient.userID;  // access from patient object
    Prescription newPrescription = Prescription(prescriptionID: -1, doctorID: doctorID, patientID: patientID, prescription: prescriptionText);

    var testPrescription = await PrescriptionService.createPrescription(newPrescription);

    // if testprescription == message:
    //     show snackbar "faield to prescirbe medcine"
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Center(child: Text("Prescription")),
          backgroundColor: Colors.redAccent,
        ),
        body: Container(
          alignment: Alignment.center,
          padding: const EdgeInsets.all(20),
          child: Column(
            children: [
              TextField(
                controller: textarea,
                keyboardType: TextInputType.multiline,
                minLines: 1,
                maxLines: null,
                decoration: const InputDecoration(
                    hintText: "Enter Prescription",
                    focusedBorder: OutlineInputBorder(
                        borderSide: BorderSide(width: 1, color: Colors.redAccent)
                    )
                ),
                onChanged: (text) {
                  setState(() {
                    prescriptionText = text;
                  });
                }
              ),

              ElevatedButton(
                  onPressed: () async {
                    await createPrescription();

                    if (!mounted) return;

                    ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Prescription Saved!'),));

                  },
                  child: const Text("Save Prescription")
              )
            ],
          ),
        )
    );
  }
}