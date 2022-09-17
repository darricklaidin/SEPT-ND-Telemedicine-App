class Appointment {

  final int appointmentID;
  final String date;
  final String startTime;
  final String endTime;
  final String appointmentStatus;
  final int doctorID;
  final int patientID;

  Appointment({
    required this.appointmentID,
    required this.date,
    required this.startTime,
    required this.endTime,
    required this.appointmentStatus,
    required this.doctorID,
    required this.patientID
  });
}