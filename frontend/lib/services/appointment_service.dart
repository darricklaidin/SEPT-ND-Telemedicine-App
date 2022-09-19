import 'package:frontend/config/constants.dart';
import 'package:frontend/models/appointment.dart';
import 'package:http/http.dart' as http;

class AppointmentService {

  static Future deleteAppointment(int appointmentID) async {
    // print("Appointment id: $appointmentID");
    var response = await http.delete(Uri.parse('$apiRootUrl/appointments/$appointmentID'));
    if (response.statusCode == 200) {
      print("Deleted appointment $appointmentID");
    }
    else {
      throw Exception('Failed to delete appointment $appointmentID');
    }
  }

  static Future bookAppointment(Appointment appointment) async {

  }

}