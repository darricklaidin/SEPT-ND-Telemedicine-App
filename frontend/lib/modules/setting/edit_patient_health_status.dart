import 'package:flutter/material.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:frontend/services/patient_service.dart';

import '../../config/themes/light_palette.dart';
import '../../models/patient.dart';

class EditPatientHealthStatusScreen extends StatefulWidget {
  const EditPatientHealthStatusScreen({Key? key}) : super(key: key);

  @override
  State<EditPatientHealthStatusScreen> createState() => _EditPatientHealthStatusScreenState();
}

class _EditPatientHealthStatusScreenState extends State<EditPatientHealthStatusScreen> {

  String healthStatus = "";

  TextEditingController healthStatusController = TextEditingController(text: "");

  Future updateHealthStatus() async {
    Patient oldPatient = await getUserFromStorage();

    Patient updatedPatient = oldPatient;
    updatedPatient.symptoms = healthStatus;

    try {
      await PatientService.updatePatient(await getUserIdFromStorage(), updatedPatient, null);

      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          content: Text("Health status updated successfully"),
          duration: Duration(seconds: 2),
          backgroundColor: LightPalette.success,
        ),
      );

      Navigator.pop(context);

    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          content: Text("Failed to update health status"),
          duration: Duration(seconds: 2),
          backgroundColor: LightPalette.error,
        ),
      );
    }

  }

  Future loadHealthStatus() async {
    Patient currentPatient = await getUserFromStorage();
    setState(() {
      if (currentPatient.symptoms != null) {
        healthStatus = currentPatient.symptoms!;
        healthStatusController.text = healthStatus;
      }
    });
  }

  @override
  void initState() {
    super.initState();
    loadHealthStatus();
  }

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;

    return GestureDetector(
      onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
      child: Scaffold(
        appBar: AppBar(
          title: const Text('Edit Patient Health Status'),
        ),
        body: SingleChildScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          child: SizedBox(
            width: width,
            height: height * 0.8,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                SizedBox(height: height * 0.1),
                Container(
                  height: height * 0.6,
                  width: width * 0.8,
                  child: TextField(
                    controller: healthStatusController,
                    textInputAction: TextInputAction.newline,
                    keyboardType: TextInputType.multiline,
                    minLines: null,
                    maxLines: null,
                    expands: true,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      filled: true,
                      labelText: 'How are you feeling today?',
                    ),
                    onChanged: (text) {
                      setState(() {
                        healthStatus = text;
                      });
                    },
                  ),
                ),
              ElevatedButton(
                  onPressed: () async {
                    await updateHealthStatus();
                  },
                  style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(LightPalette.secondary),
                  ),
                  child: const Text('Save',
                    style: TextStyle(fontWeight: FontWeight.bold),),
                ),

              ],
            ),
          ),
        ),
      ),
    );
  }
}
