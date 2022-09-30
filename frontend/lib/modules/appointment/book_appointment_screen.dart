import 'package:flutter/material.dart';
import 'package:booking_calendar/booking_calendar.dart';
import 'package:date_generator/date_generator.dart';
import 'package:frontend/services/auth_service.dart';

import '../../models/appointment.dart';
import '../../models/doctor.dart';
import '../../models/patient.dart';
import '../../services/doctor_service.dart';
import 'package:frontend/models/availability.dart';


class BookAppointmentScreen extends StatefulWidget {
  final Doctor doctor;
  const BookAppointmentScreen({Key? key, required this.doctor}) : super(key: key);

  @override
  State<BookAppointmentScreen> createState() => _BookAppointmentScreenState();
}

class _BookAppointmentScreenState extends State<BookAppointmentScreen> {

  late BookingService mockBookingService;
  final int END_TIME_OFFSET = 1;

  bool isLoading = true;

  // doctor's appointment
  List<Appointment> doctorAppointments = [];
  // doctor's availability
  List<Availability> doctorAvailabilities = [];

  Stream<dynamic>? getBookingStreamMock(
      {required DateTime end, required DateTime start}) {
    // Load existing doctor appointments
    return Stream.value(doctorAppointments);
  }

  List<DateTimeRange> convertedBookedSlots = [];

  List<DateTimeRange> convertStreamResultMock({required dynamic streamResult}) {
    // This is where we convert the stream result of existing doctor appointments to a list of DateTimeRange
    streamResult.forEach((appointment) {
      convertedBookedSlots.add(DateTimeRange(
          start: DateTime(appointment.date.year, appointment.date.month, appointment.date.day,
              appointment.startTime.hour, appointment.startTime.minute),
          end: DateTime(appointment.date.year, appointment.date.month, appointment.date.day,
              appointment.endTime.hour, appointment.endTime.minute)));
    });

    return convertedBookedSlots;
  }

  Future<dynamic> uploadBookingMock(
      {required BookingService newBooking}) async {

    // This will be where we upload the booking to the database
    await Future.delayed(const Duration(seconds: 1));

    // appointment id

    // current patient user
    Patient patient = await getUserFromStorage();

    // doctor who the appointment is made for
    Doctor doctor = widget.doctor;

    // date
    DateTime date = DateTime(newBooking.bookingStart.year, newBooking.bookingStart.month, newBooking.bookingStart.day);

    // start time
    TimeOfDay startTime = TimeOfDay(hour: newBooking.bookingStart.hour, minute: newBooking.bookingStart.minute);

    // end time
    TimeOfDay endTime = TimeOfDay(hour: newBooking.bookingEnd.hour, minute: newBooking.bookingEnd.minute);

    // appointment status
    String appointmentStatus = "UPCOMING"; // COMPLETED, ONGOING, UPCOMING
    if (DateTime(newBooking.bookingStart.year, newBooking.bookingStart.month,
        newBooking.bookingStart.day, newBooking.bookingStart.hour,
        newBooking.bookingStart.minute).isBefore(DateTime.now())) {
      appointmentStatus = "COMPLETED";
    } else if (DateTime(newBooking.bookingStart.year, newBooking.bookingStart.month,
        newBooking.bookingStart.day, newBooking.bookingStart.hour,
        newBooking.bookingStart.minute).isAfter(DateTime.now())) {
      appointmentStatus = "UPCOMING";
    } else {
      appointmentStatus = "ONGOING";
    }

    Appointment newAppointment = Appointment(
      appointmentID: -1,
      patient: patient,
      doctor: doctor,
      date: date,
      startTime: startTime,
      endTime: endTime,
      appointmentStatus: appointmentStatus,
    );

    convertedBookedSlots.add(DateTimeRange(
        start: newBooking.bookingStart, end: newBooking.bookingEnd));

    print('$newAppointment has been uploaded');
    // print('${newAppointment.toJson()} has been uploaded');


  }

  List<DateTimeRange> generatePauseSlots() {
    // This is where we generate the doctor's unavailability time slots
    Set availableDates = generateDates();

    List<DateTimeRange> pauseSlots = [];
    for (var date in availableDates) {
      // Get the availability for date's day of week
      doctorAvailabilities.where((availability) => availability.dayOfWeek == date.weekday).forEach((availability) {
        DateTime doctorStartAvailable = DateTime(date.year, date.month, date.day, availability.startTime.hour, availability.startTime.minute);
        DateTime doctorEndAvailable = DateTime(date.year, date.month, date.day, availability.endTime.hour, availability.endTime.minute);

        // Add the pause slots
        pauseSlots.add(DateTimeRange(
            start: DateTime(date.year, date.month, date.day, 0, 0),
            end:  doctorStartAvailable)
        );

        pauseSlots.add(DateTimeRange(
            start: doctorEndAvailable,
            end: DateTime(date.year, date.month, date.day, 23, 59))
        );
      });
    }

    return pauseSlots;
  }

  Set generateDates() {
    Set availableDates = {};
    Set daysOfWeekAvailable = {};

    // Get days of week where doctor is unavailable
    for (Availability availability in doctorAvailabilities) {
      daysOfWeekAvailable.add(availability.dayOfWeek);
    }
    print("Days of week available $daysOfWeekAvailable");

    for (int year = DateTime.now().year; year < DateTime.now().year + 1; year++) {
      for (int month = 1; month <= 12; month++) {
        for (int week = 1; week <= 5; week++) {
          for (int weekDay = 1; weekDay <= 7; weekDay++) {
            if (daysOfWeekAvailable.contains(weekDay)) {
              weekDay = weekDay == 7 ? 1 : weekDay + 1;
              DateTime generatedDate = Generator().week(week).weekDay(weekDay).month(month).of(year);
              availableDates.add(generatedDate);
            }
          }
        }
      }
    }

    return availableDates;

  }

  List<int> generateDisabledDays() {
    // Based on doctor's availability
    Set daysOfWeekAvailable = {};
    Set daysOfWeekUnavailable = {1, 2, 3, 4, 5, 6, 7};


    for (Availability availability in doctorAvailabilities) {
      daysOfWeekAvailable.add(availability.dayOfWeek);
    }

    daysOfWeekUnavailable.removeAll(daysOfWeekAvailable);

    return List<int>.from(daysOfWeekUnavailable);
  }

  Future loadDoctorAppointments() async {
    doctorAppointments = await DoctorService.fetchDoctorAppointments(widget.doctor.userID);
  }

  Future loadDoctorAvailabilities() async {
    doctorAvailabilities = await DoctorService.fetchDoctorAvailabilities(widget.doctor.userID);
  }

  Future loadDoctorData() async {
    setState(() {
      isLoading = true;
    });
    await loadDoctorAppointments();
    await loadDoctorAvailabilities();
    setState(() {
      isLoading = false;
    });
  }

  @override
  void initState() {
    super.initState();

    // Initialize doctor data
    loadDoctorData();

    // Initialize booking service
    mockBookingService = BookingService(
      serviceName: 'Mock Service',
      // Length of each appointment
      serviceDuration: 30,
      // Default start time range
      bookingStart: DateTime(DateTime.now().year, DateTime.now().month, DateTime.now().day, 0, 0),
      // Default end time range
      bookingEnd: DateTime(DateTime.now().year, DateTime.now().month, DateTime.now().day, 24, 0),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Book Appointment'),
      ),
      body: Builder (
        builder: (context) {
          if (isLoading) {
            return const Center(child: CircularProgressIndicator());
          } else {
            return BookingCalendar(
              bookingService: mockBookingService,
              convertStreamResultToDateTimeRanges: convertStreamResultMock,
              getBookingStream: getBookingStreamMock,
              uploadBooking: uploadBookingMock,
              hideBreakTime: false,
              pauseSlots: generatePauseSlots(),
              pauseSlotText: 'Unavailable',
              loadingWidget: const Text("Fetching data..."),
              uploadingWidget: const Center(child: CircularProgressIndicator()),
              startingDayOfWeek: StartingDayOfWeek.monday,
              disabledDays: generateDisabledDays(),
            );
          }
        }
      ),
    );
  }

}
