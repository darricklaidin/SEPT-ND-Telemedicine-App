import 'package:frontend/models/doctor.dart';
import 'package:frontend/models/patient.dart';

class Prescription {
  final int prescriptionID;
  final int doctorID;
  final int patientID;
  final String prescription;

  Prescription({
    required this.prescriptionID,
    required this.doctorID,
    required this.patientID,
    required this.prescription,
  });


  factory Prescription.fromJson(Map<String, dynamic> json, Doctor doctor, Patient patient) {
    return Prescription(
      prescriptionID: json['prescriptionID'],
      doctorID: json['doctorID'],
      patientID: json['patientID'],
      prescription: json['prescription'],
    );
  }

  toJson() {
    return {
      'prescriptionID': prescriptionID,
      'doctorID': doctorID,
      'patientID': patientID,
      'prescription': prescription,
    };
  }
}