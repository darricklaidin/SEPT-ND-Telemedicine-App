import 'package:flutter/material.dart';
import 'package:booking_calendar/booking_calendar.dart';
import 'package:date_generator/date_generator.dart';

import 'package:frontend/config/themes/light_palette.dart';
import 'package:frontend/main.dart';
import 'package:frontend/services/appointment_service.dart';
import 'package:frontend/services/auth_service.dart';
import '../../models/appointment.dart';
import '../../models/doctor.dart';
import '../../models/patient.dart';
import '../../services/doctor_service.dart';
import 'package:frontend/models/availability.dart';

class BookAppointmentScreen extends StatefulWidget {
  final AuthService authService;

  final Doctor doctor;
  const BookAppointmentScreen(
      {Key? key, required this.doctor, required this.authService})
      : super(key: key);

  @override
  State<BookAppointmentScreen> createState() => _BookAppointmentScreenState();
}

class _BookAppointmentScreenState extends State<BookAppointmentScreen> {
  late BookingService mockBookingService;

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
          start: DateTime(
              appointment.date.year,
              appointment.date.month,
              appointment.date.day,
              appointment.startTime.hour,
              appointment.startTime.minute),
          end: DateTime(
              appointment.date.year,
              appointment.date.month,
              appointment.date.day,
              appointment.endTime.hour,
              appointment.endTime.minute)));
    });

    return convertedBookedSlots;
  }

  Future<dynamic> uploadBookingMock(
      {required BookingService newBooking}) async {
    // current patient user
    Patient? patient = await widget.authService.getUserFromStorage();

    // doctor who the appointment is made for
    Doctor doctor = widget.doctor;

    // Check that users exist
    if (patient == null) {
      await widget.authService.logoutUser();
      if (!mounted) return;
      Navigator.of(context, rootNavigator: true).pushAndRemoveUntil(
          MaterialPageRoute(builder: (context) => const MyApp()),
          (route) => route.isFirst);
      return;
    }

    if (await DoctorService.fetchDoctor(doctor.userID) ==
        "Resource Not Found") {
      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          content:
              Text("Failed to create appointment. Doctor no longer exists."),
          duration: Duration(seconds: 2),
          backgroundColor: LightPalette.error,
        ),
      );

      return;
    }

    // date
    DateTime date = DateTime(newBooking.bookingStart.year,
        newBooking.bookingStart.month, newBooking.bookingStart.day);

    // start time
    TimeOfDay startTime = TimeOfDay(
        hour: newBooking.bookingStart.hour,
        minute: newBooking.bookingStart.minute);

    // end time
    TimeOfDay endTime = TimeOfDay(
        hour: newBooking.bookingEnd.hour, minute: newBooking.bookingEnd.minute);

    // appointment status
    String appointmentStatus = "UPCOMING"; // COMPLETED, ONGOING, UPCOMING
    if (DateTime(
            newBooking.bookingStart.year,
            newBooking.bookingStart.month,
            newBooking.bookingStart.day,
            newBooking.bookingStart.hour,
            newBooking.bookingStart.minute)
        .isBefore(DateTime.now())) {
      appointmentStatus = "COMPLETED";
    } else if (DateTime(
            newBooking.bookingStart.year,
            newBooking.bookingStart.month,
            newBooking.bookingStart.day,
            newBooking.bookingStart.hour,
            newBooking.bookingStart.minute)
        .isAfter(DateTime.now())) {
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

    dynamic response =
        await AppointmentService.createAppointment(newAppointment);

    // Check if response is successful; show snackbar
    if (!mounted) return;

    if (!response[0]) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: const EdgeInsets.only(bottom: 10.0),
          content: Text(response[1]),
          duration: const Duration(seconds: 2),
          backgroundColor: LightPalette.error,
        ),
      );
    } else {
      // cross out selected time slot from selection from appointment creation is successful
      convertedBookedSlots.add(DateTimeRange(
          start: newBooking.bookingStart, end: newBooking.bookingEnd));

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          content: Text("Appointment created successfully."),
          duration: Duration(seconds: 2),
          backgroundColor: LightPalette.success,
        ),
      );
    }
  }

  List<DateTimeRange> generatePauseSlots() {
    // This is where we generate the doctor's unavailability time slots
    Set availableDates = generateDates();

    List<DateTimeRange> pauseSlots = [];
    for (var date in availableDates) {
      // Get the availability for date's day of week
      doctorAvailabilities
          .where((availability) => availability.dayOfWeek == date.weekday)
          .forEach((availability) {
        DateTime doctorStartAvailable = DateTime(
            date.year,
            date.month,
            date.day,
            availability.startTime.hour,
            availability.startTime.minute);
        DateTime doctorEndAvailable = DateTime(date.year, date.month, date.day,
            availability.endTime.hour, availability.endTime.minute);

        // Add the pause slots
        pauseSlots.add(DateTimeRange(
            start: DateTime(date.year, date.month, date.day, 0, 0),
            end: doctorStartAvailable));

        pauseSlots.add(DateTimeRange(
            start: doctorEndAvailable,
            end: DateTime(date.year, date.month, date.day, 23, 59)));
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

    for (int year = DateTime.now().year;
        year < DateTime.now().year + 1;
        year++) {
      for (int month = 1; month <= 12; month++) {
        for (int week = 1; week <= 5; week++) {
          for (int weekDay = 1; weekDay <= 7; weekDay++) {
            if (daysOfWeekAvailable.contains(weekDay)) {
              weekDay = weekDay == 7 ? 1 : weekDay + 1;
              DateTime generatedDate =
                  Generator().week(week).weekDay(weekDay).month(month).of(year);
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
    doctorAppointments =
        await DoctorService.fetchDoctorAppointments(widget.doctor.userID);
  }

  Future loadDoctorAvailabilities() async {
    doctorAvailabilities =
        await DoctorService.fetchDoctorAvailabilities(widget.doctor.userID);
  }

  Future loadDoctorData() async {
    setState(() {
      isLoading = true;
    });
    await loadDoctorAppointments();
    await loadDoctorAvailabilities();

    if (!mounted) return;
    if (doctorAvailabilities.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          content: Text("Doctor has no availability."),
          duration: Duration(seconds: 2),
          backgroundColor: LightPalette.error,
        ),
      );
      Navigator.pop(context);
    }

    _initBookingService();

    setState(() {
      isLoading = false;
    });
  }

  _getNextAvailableDay() {
    int result = -1;

    // check if there are available days for the rest of the week
    for (int i = DateTime.now().weekday; i <= 7; ++i) {
      for (Availability availability in doctorAvailabilities) {
        if (availability.dayOfWeek == i) {
          // check if availability endtime for the day is past
          if (_timeOfDayToDouble(availability.endTime) >
              _timeOfDayToDouble(TimeOfDay.now())) {
            if (i == DateTime.now().weekday) {
              result = DateTime.now().day;
            } else {
              // get day of with the specified availability (if not today)
              result = _getDateofNextDayType(i);
            }
            break;
          }
        }
      }
    }

    if (result == -1) {
      // if no days found this week check next week
      for (int i = DateTime.now().weekday - 1; i >= 1; --i) {
        for (Availability availability in doctorAvailabilities) {
          if (availability.dayOfWeek == i) {
            if (_timeOfDayToDouble(availability.endTime) >
                _timeOfDayToDouble(TimeOfDay.now())) {
              // get day of with the specified availability
              result = _getDateofNextDayType(i);
              break;
            }
          }
        }
      }
    }

    return result;
  }

  _getDateofNextDayType(int dayOfWeek) {
    return DateTime.now()
        .add(Duration(
          days: (dayOfWeek - DateTime.now().weekday) % DateTime.daysPerWeek,
        ))
        .day;
  }

  double _timeOfDayToDouble(TimeOfDay myTime) {
    return myTime.hour + myTime.minute / 60.0;
  }

  _initBookingService() {
    int nextAvailableDay = _getNextAvailableDay();

    // Initialize booking service
    mockBookingService = BookingService(
      serviceName: 'Mock Service',
      // Length of each appointment
      serviceDuration: 30,
      // Default start time range
      bookingStart: DateTime(
          DateTime.now().year, DateTime.now().month, nextAvailableDay, 0, 0),
      // Default end time range
      bookingEnd: DateTime(
          DateTime.now().year, DateTime.now().month, nextAvailableDay, 24, 0),
    );
  }

  @override
  void initState() {
    super.initState();

    // Initialize doctor data
    loadDoctorData();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Book Appointment'),
      ),
      body: Builder(builder: (context) {
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
      }),
    );
  }
}
