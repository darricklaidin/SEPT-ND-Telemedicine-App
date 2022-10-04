import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:persistent_bottom_nav_bar/persistent-tab-view.dart';

import 'package:frontend/models/prescription.dart';
import '../../models/api_response.dart';
import '../../config/themes/light_palette.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:frontend/services/prescription_service.dart';
 

class PrescriptionScreen extends StatefulWidget {
  const PrescriptionScreen({Key? key}) : super(key: key);
  
  @override
  State<PrescriptionScreen> createState() => _PrescriptionScreen();
}

class _PrescriptionScreen extends State<PrescriptionScreen> {
  List<Prescription> prescriptions = List<Prescription>.empty(growable: true);
  bool isLoading = true;

  final textarea = TextEditingController();
  String text = "";

  void _setText() {
    setState(() {
      text = textarea.text;
    });
  }

  @override
  void initState() {
    super.initState();
  }
  
  String addPrescription = "";
  
  Future createPrescription(prescription) async {
    addPrescription = await PrescriptionService.createPrescription(prescription);
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
                maxLines: 4,
                decoration: const InputDecoration(
                    hintText: "Enter Prescription",
                    focusedBorder: OutlineInputBorder(
                        borderSide: BorderSide(width: 1, color: Colors.redAccent)
                    )
                ),

              ),

              ElevatedButton(
                  onPressed: (){
                    ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Prescription Saved!'),));
                    _setText();
                    createPrescription(context);
                    print(textarea.text);
                  },
                  child: const Text("Save Prescription")
              )
            ],
          ),
        )
    );
  }
}