import 'dart:convert';
import 'package:http/http.dart' as http;
import 'dart:async';

import 'package:frontend/config/constants.dart';
import 'package:frontend/services/doctor_service.dart';
import 'package:frontend/services/patient_service.dart';
import '../models/doctor.dart';
import '../models/patient.dart';
import '../models/prescription.dart';

class PrescriptionService {

  static Future findPrescriptionByID(int prescriptionID) async {
    var response = await http
        .get(Uri.parse('$apiPrescriptionRootUrl/prescriptions/$prescriptionID'));
    if (response.statusCode == 200) {
      if (await getPrescriptionFromJSON(jsonDecode(response.body)) == null) {
        // When one of the users gets deleted, Delete prescription with
        // this prescription id from database
      }
    } else if (response.statusCode == 404) {
      return jsonDecode(response.body)['message'];
    } else {
      throw Exception('Failed to find prescription $prescriptionID');
    }
  }

  static Future fetchPatientPrescriptions(int patientID) async {
    var response = await http
        .get(Uri.parse('$apiPrescriptionRootUrl/prescriptions/patient/$patientID'))
        .timeout(const Duration(seconds: 5));

    if (response.statusCode == 200) {
      List<dynamic> jsonData = jsonDecode(response.body)['content'];
      List<Prescription> prescriptions = [];
      for (dynamic prescription in jsonData) {
        Prescription? tempPrescription =
        await getPrescriptionFromJSON(prescription);
        if (tempPrescription != null) {
          prescriptions.add(tempPrescription);
        } else {
          // Prescription should be deleted from database
        }
      }
      return prescriptions;
    } else {
      throw Exception('Failed to load prescriptions');
    }
  }


  static Future createPrescription(Prescription prescription) async {
    var response = await http.post(
      Uri.parse('$apiPrescriptionRootUrl/prescriptions'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(prescription.toJson()),
    );

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
