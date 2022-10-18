import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:frontend/config/themes/light_palette.dart';

import '../../models/patient.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:frontend/models/prescription.dart';
import 'package:frontend/services/prescription_service.dart';

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

    int doctorID = await AuthService.getUserIdFromStorage();  // access from getuserIDfromstorage
    int patientID = widget.patient.userID;  // access from patient object
    Prescription newPrescription = Prescription(prescriptionID: -1, doctorID: doctorID, patientID: patientID, prescription: prescriptionText);

    await PrescriptionService.createPrescription(newPrescription);
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text("Prescription"),
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
                        borderSide: BorderSide(width: 1)
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

                    ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(
                          behavior: SnackBarBehavior.floating,
                          margin: EdgeInsets.only(bottom: 10.0),
                          content: Text("Prescription saved!"),
                          duration: Duration(seconds: 2),
                          backgroundColor: LightPalette.success,
                        ),
                    );

                    Navigator.pop(context);
                  },
                  child: const Text("Save Prescription",
                    style: TextStyle(fontWeight: FontWeight.bold),)
              )
            ],
          ),
        )
    );
  }

}