import 'package:flutter/material.dart';
import 'package:syncfusion_flutter_datepicker/datepicker.dart';
import 'package:time_picker_widget/time_picker_widget.dart';

class BookingScreen extends StatefulWidget {
  const BookingScreen({super.key});

  @override
  State<BookingScreen> createState() => _BookingScreenState();
}

class _BookingScreenState extends State<BookingScreen> {
  int _index = 0;

  final List<DateTime> UNAVAILABE_DATES = [
    DateTime(2022, 09, 20),
    DateTime(2022, 09, 21)
  ];

  // list the available hours from the backend as INT values in 24 hr format
  final List<int> _availableHours = [4, 5, 6, 7, 10, 11, 13, 14, 16, 19];

  // hardcode this list(from this you can get the slots hourly)
  final List<int> _availableMinutes = [0];

  // selected time - in 12 hr format
  var selectedTime;

  @override
  Widget build(BuildContext context) {
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
              if (_index <= 1) {
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
            steps: _buildSteps()));
  }

  List<Step> _buildSteps() {
    return <Step>[
      Step(
          title: const Text('Select Date'),
          content: SfDateRangePicker(
            minDate: DateTime.now(),
            monthViewSettings: DateRangePickerMonthViewSettings(
                blackoutDates: UNAVAILABE_DATES),
          )),
      Step(
          title: Text('Select time'),
          content: Center(
              child: TextButton(
            onPressed: () => showCustomTimePicker(
                context: context,
                onFailValidation: (context) =>
                    Text('this time slot is already booked'),
                //for the initial time you must set a value inside the _availableHours list
                initialTime: TimeOfDay(hour: 14, minute: 0),
                selectableTimePredicate: (time) =>
                    _availableHours.indexOf(time!.hour) != -1 &&
                    _availableMinutes.indexOf(time.minute) != -1).then(
                (time) => setState(() => selectedTime = time?.format(context))),
            child: Text(
              selectedTime ?? 'Select Time',
              style: TextStyle(fontSize: 24, fontWeight: FontWeight.w600),
            ),
          ))),
      Step(
        title: const Text('Confirm'),
        content: TextButton(
          onPressed: () {
            Navigator.pushNamed(context, '/');
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
