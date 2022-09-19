import 'package:flutter/material.dart';
import 'package:syncfusion_flutter_datepicker/datepicker.dart';
import 'package:time_picker_widget/time_picker_widget.dart';

class BookingScreen extends StatefulWidget {
  // Doctor doctor;

  const BookingScreen({super.key});

  @override
  State<BookingScreen> createState() => _BookingScreenState();
}

class _BookingScreenState extends State<BookingScreen> {
  int _index = 0;

  // TODO: Dynamically generate these depending on doctor's availability and user appointments

  final List<DateTime> UNAVAILABE_DATES = [
    DateTime(2022, 09, 20),
    DateTime(2022, 09, 21)
  ];

  // list the available hours from the backend as INT values in 24 hr format
  final List<int> _availableHours = [4, 5, 6, 7, 10, 11, 13, 14, 16, 19];
  // hardcode this list(from this you can get the slots hourly)
  final List<int> _availableMinutes = [0];

  // selected time - in 12 hr format
  TimeOfDay? selectedStartTime;
  TimeOfDay? selectedEndTime;

  var selectedDate;

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
            monthViewSettings: DateRangePickerMonthViewSettings(blackoutDates: UNAVAILABE_DATES),
            selectionMode: DateRangePickerSelectionMode.single,
            monthCellStyle: const DateRangePickerMonthCellStyle(
              blackoutDateTextStyle: TextStyle(color: Colors.grey),
            ),
            showNavigationArrow: true,
            showTodayButton: true,
            onSelectionChanged: (DateRangePickerSelectionChangedArgs args) {
              print(args.value);
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
                    print(selectedStartTime);
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
                    print(selectedEndTime);
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
          onPressed: () {
            // TODO: Make appointment in backend

            // Navigator.of(context).pop();
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
