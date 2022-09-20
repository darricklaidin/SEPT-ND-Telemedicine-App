import 'package:flutter/material.dart';
import 'package:frontend/services/appointment_service.dart';
import 'package:syncfusion_flutter_datepicker/datepicker.dart';
import 'package:time_picker_widget/time_picker_widget.dart';

import '../../models/appointment.dart';
import '../../models/doctor.dart';
import '../../models/patient.dart';
import 'package:frontend/models/availability.dart';
import 'package:frontend/services/doctor_service.dart';

class BookAppointmentScreen extends StatefulWidget {
  // Doctor doctor;

  const BookAppointmentScreen({super.key});

  @override
  State<BookAppointmentScreen> createState() => _BookAppointmentScreenState();
}

class _BookAppointmentScreenState extends State<BookAppointmentScreen> {
  int _index = 0;

  bool isLoading = true;

  // TODO: Use authentication for doctor and patient

  // Hardcoded doctor and patient values do not matter except for userID
  Doctor doctor = Doctor(
    userID: 1,
    firstName: "John",
    lastName: "Doe",
    email: "johndoe@gmail.com",
    specialty: "Cardiologist",
    dateOfBirth: DateTime(1990, 1, 1),
    accountStatus: "Active",
  );

  Patient patient = Patient(
    userID: 1,
    firstName: "Jane",
    lastName: "Doe",
    email: "janedoe@gmail.com",
    dateOfBirth: DateTime(1990, 1, 1),
    accountStatus: "Active",
  );

  List<int> daysOfWeekAvailable = List<int>.empty(growable: true);

  // selected time - in 12 hr format
  TimeOfDay? selectedStartTime;
  TimeOfDay? selectedEndTime;

  var selectedDate;

  static Future bookAppointment(DateTime date, TimeOfDay startTime, TimeOfDay endTime,
      Doctor doctor, Patient patient) async {
    Appointment newAppointment = Appointment(
        appointmentID: null,
        date: date,
        startTime: startTime,
        endTime: endTime,
        appointmentStatus: "UPCOMING",
        doctor: doctor,
        patient: patient);

    String status = await AppointmentService.bookAppointment(newAppointment);

    if (status == "success") {
      return "success";
    }
    else if (status == "time clash") {
      return "time clash";
    }
    else if (status == "doctor unavailable") {
      return "doctor unavailable";
    }
    else {
      return "error";
    }
  }

  void initializeCalendarRestrictions(int doctorID, int patientID) async {
    // Load doctor availabilities from backend
    List<Availability> doctorAvailabilities = await DoctorService.fetchDoctorAvailabilities(doctorID);
    setState(() {
      for (Availability availability in doctorAvailabilities) {
        daysOfWeekAvailable.add(availability.dayOfWeek!);
      }
      isLoading = false;
    });
  }

  @override
  void initState() {
    super.initState();
    initializeCalendarRestrictions(doctor.userID, 1);

  }

  @override
  Widget build(BuildContext context) {
    if (isLoading) {
      return const Padding(
        padding: EdgeInsets.fromLTRB(0, 100, 0, 0),
        child: Center(
          child: CircularProgressIndicator(),
        ),
      );
    }
    return Scaffold(
        body: Stepper(
            currentStep: _index,
            onStepCancel: () {
              if (_index > 0) {
                setState(() {
                  _index -= 1;
                });
              }
            },
            onStepContinue: () {
              if (_index <= 2) {
                // logic for checking valid date
                setState(() {
                  _index += 1;
                });
              }
            },
            onStepTapped: (int index) {
              setState(() {
                _index = index;
              });
            },
            steps: _buildSteps(),
            controlsBuilder: (context, _) {
              if (_index == 3) {
                return Row(
                  children: <Widget>[
                    Container(),
                    TextButton(
                      onPressed: _.onStepCancel,
                      child: const Text('BACK'),
                    ),
                  ],
                );
              }
              else if (_index == 0) {
                return Row(
                  children: <Widget>[
                    TextButton(
                      onPressed: _.onStepContinue,
                      child: const Text('NEXT'),
                    ),
                    TextButton(
                      onPressed: Navigator.of(context).pop,
                      child: const Text('CANCEL'),
                    ),
                  ],
                );
              }
              return Row(
                children: <Widget>[
                  TextButton(
                    onPressed: _.onStepContinue,
                    child: const Text('NEXT'),
                  ),
                  TextButton(
                    onPressed: _.onStepCancel,
                    child: const Text('BACK'),
                  ),
                ],
              );
            }
        )
    );
  }

  List<Step> _buildSteps() {
    return <Step>[
      Step(
          title: const Text('Select Date'),
          content: SfDateRangePicker(
            minDate: DateTime.now(),
            monthViewSettings: const DateRangePickerMonthViewSettings(
              firstDayOfWeek: 1,
            ),
            selectableDayPredicate: (date) {
              return daysOfWeekAvailable.contains(date.weekday);
            },
            selectionMode: DateRangePickerSelectionMode.single,
            monthCellStyle: const DateRangePickerMonthCellStyle(
              blackoutDateTextStyle: TextStyle(color: Colors.grey),
            ),
            showNavigationArrow: true,
            showTodayButton: true,
            onSelectionChanged: (DateRangePickerSelectionChangedArgs args) {
              setState(() {
                selectedDate = args.value;
              });
            },
          )
      ),
      Step(
          title: Text('Select start time'),
          content: Center(
              child: TextButton(
                style: ButtonStyle(
                  padding: MaterialStateProperty.all<EdgeInsets>(const EdgeInsets.all(10)),
                  backgroundColor: MaterialStateProperty.all<Color>(Colors.amber),
                  foregroundColor: MaterialStateProperty.all<Color>(Colors.white),
                ),
                onPressed: () {
                  showCustomTimePicker(
                    context: context,
                    onFailValidation: (context) => const Text('This time slot is already booked'),
                    // For the initial time you must set a value inside the _availableHours list
                    initialTime: TimeOfDay(hour: 14, minute: 0),
                    selectableTimePredicate: (time) => true,
                  ).then((time) => setState(() {
                    selectedStartTime = time;
                    // print(selectedStartTime);
                  }));
                },
                child: Text(
                  selectedStartTime?.format(context) ?? 'Select start time',
                  style: const TextStyle(fontSize: 16),
                ),
              )
          )
      ),
      Step(
          title: Text('Select end time'),
          content: Center(
              child: TextButton(
                style: ButtonStyle(
                  padding: MaterialStateProperty.all<EdgeInsets>(const EdgeInsets.all(10)),
                  backgroundColor: MaterialStateProperty.all<Color>(Colors.amber),
                  foregroundColor: MaterialStateProperty.all<Color>(Colors.white),
                ),
                onPressed: () {
                  showCustomTimePicker(
                    context: context,
                    onFailValidation: (context) => const Text('This time slot is already booked'),
                    // For the initial time you must set a value inside the _availableHours list
                    initialTime: TimeOfDay(hour: 14, minute: 0),
                    selectableTimePredicate: (time) => true,
                  ).then((time) => setState(() {
                    selectedEndTime = time;
                    // print(selectedEndTime);
                  }));
                },
                child: Text(
                  selectedEndTime?.format(context) ?? 'Select end time',
                  style: const TextStyle(fontSize: 16),
                ),
              )
          )
      ),
      Step(
        title: const Text('Confirm'),
        content: TextButton(
          onPressed: () async {
            if (selectedDate == null || selectedStartTime == null || selectedEndTime == null) {
              ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('Please select a date and time')));
            }
            else {
              // Make appointment in backend

              String status = await bookAppointment(selectedDate, selectedStartTime!, selectedEndTime!, doctor, patient);
              if (status == "success") {
                if (!mounted) return;
                ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                      behavior: SnackBarBehavior.floating,
                      margin: EdgeInsets.only(bottom: 10.0),
                      content: Text("Appointment successfully made"),
                      duration: Duration(seconds: 2),
                    )
                );
                Navigator.of(context).pop();
              }
              else if (status == "time clash") {
                if (!mounted) return;
                ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                      behavior: SnackBarBehavior.floating,
                      margin: EdgeInsets.only(bottom: 10.0),
                      content: Text("Appointment could not be made; Time Clash"),
                      duration: Duration(seconds: 2),
                    )
                );
              }
              else if (status == "doctor unavailable") {
                if (!mounted) return;
                ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                      behavior: SnackBarBehavior.floating,
                      margin: EdgeInsets.only(bottom: 10.0),
                      content: Text("Appointment could not be made; Doctor Unavailable"),
                      duration: Duration(seconds: 2),
                    )
                );
              }
              else {
                if (!mounted) return;
                ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                      behavior: SnackBarBehavior.floating,
                      margin: EdgeInsets.only(bottom: 10.0),
                      content: Text("Appointment could not be made; Error"),
                      duration: Duration(seconds: 2),
                    )
                );
              }
            }
          },
          style: ButtonStyle(
            backgroundColor: MaterialStateProperty.all(
                const Color.fromARGB(255, 237, 201, 94)),
            padding: MaterialStateProperty.all(
              const EdgeInsets.symmetric(horizontal: 65, vertical: 0),
            ),
            shape: MaterialStateProperty.all<RoundedRectangleBorder>(
              RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(8.0),
              ),
            ),
          ),
          child: const Text(
            'Book Appointment',
            style: TextStyle(
              color: Colors.white,
              fontSize: 14,
            ),
          ),
        ),
      ),
    ];
  }
}
