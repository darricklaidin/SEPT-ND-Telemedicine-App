import 'dart:convert';
import 'package:frontend/models/api_response.dart';
import 'package:http/http.dart' as http;
import 'dart:async';

import 'package:frontend/config/constants.dart';
import 'package:frontend/services/doctor_service.dart';
import 'package:frontend/services/patient_service.dart';
import '../models/appointment.dart';
import '../models/doctor.dart';
import '../models/patient.dart';
import '../models/prescription.dart';

class PrescriptionService {

  static Future findPrescriptionByID(int prescriptionID) async {
    var response = await http
        .get(Uri.parse('$apiPrescriptionRootUrl/prescriptions/$prescriptionID'));
    if (response.statusCode == 200) {
      if (await getPrescriptionFromJSON(jsonDecode(response.body)) == null) {
        // TODO: When one of the users gets deleted, Delete prescription with this appointment id from database
      }
    } else if (response.statusCode == 404) {
      return jsonDecode(response.body)['message'];
    } else {
      throw Exception('Failed to find prescription $prescriptionID');
    }
  }

  static Future createPrescription(Prescription prescription) async {
    // print(jsonEncode(prescription.toJson()));

    var response = await http.post(
      Uri.parse('$apiPrescriptionRootUrl/prescriptions'),
      body: jsonEncode(prescription.toJson()),
    );

    print(response.statusCode);
    print(jsonDecode(response.body));


    if (response.statusCode == 200) {
      return jsonDecode(response.body);
    } else if (response.statusCode == 400) {
      return jsonDecode(response.body)['message'];
    } else {
      return 'Failed to create prescription';
    }
  }

  static Future deletePrescription(int prescriptionID) async {
    var response = await http
        .delete(Uri.parse('$apiPrescriptionRootUrl/prescriptions/$prescriptionID'));
    if (response.statusCode == 200) {
      print("Deleted prescription $prescriptionID");
    } else {
      throw Exception('Failed to delete prescription $prescriptionID');
    }
  }

  static Future getPrescriptionFromJSON(prescription) async {
    dynamic tempDoctor = await DoctorService.fetchDoctor(prescription['doctorID']);
    if (tempDoctor == "Resource Not Found") {
      return null;
    }
    Doctor doctor = tempDoctor;

    dynamic tempPatient = await PatientService.fetchPatient(prescription['patientID']);
    if (tempPatient == "Resource Not Found") {
      return null;
    }
    Patient patient = tempPatient;

    return Prescription.fromJson(prescription, doctor, patient);
  }

}
