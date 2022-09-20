import 'package:frontend/config/constants.dart';
import 'package:http/http.dart' as http;

class AppointmentService {
  static Future deleteAppointment(int appointmentID) async {
    var response = await http
        .delete(Uri.parse('$apiBookingRootUrl/appointments/$appointmentID'));
    if (response.statusCode == 200) {
      print("Deleted appointment $appointmentID");
    } else {
      throw Exception('Failed to delete appointment $appointmentID');
    }
  }
}
