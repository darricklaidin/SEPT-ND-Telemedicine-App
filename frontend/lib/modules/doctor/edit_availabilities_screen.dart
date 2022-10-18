import 'package:flutter/material.dart';
import 'package:frontend/models/availability.dart';
import 'package:frontend/services/availability_service.dart';
import 'package:frontend/services/doctor_service.dart';
import 'package:frontend/utility.dart';
import 'package:interval_time_picker/interval_time_picker.dart';

import '../../config/themes/light_palette.dart';
import '../../services/auth_service.dart';

class EditAvailabilityScreen extends StatefulWidget {
  const EditAvailabilityScreen({Key? key}) : super(key: key);

  @override
  State<EditAvailabilityScreen> createState() => _EditAvailabilityScreenState();
}

class _EditAvailabilityScreenState extends State<EditAvailabilityScreen> {

  // Set default to null
  TimeOfDay? startTimeMonday;
  TimeOfDay? endTimeMonday;

  TimeOfDay? startTimeTuesday;
  TimeOfDay? endTimeTuesday;

  TimeOfDay? startTimeWednesday;
  TimeOfDay? endTimeWednesday;

  TimeOfDay? startTimeThursday;
  TimeOfDay? endTimeThursday;

  TimeOfDay? startTimeFriday;
  TimeOfDay? endTimeFriday;

  TimeOfDay? startTimeSaturday;
  TimeOfDay? endTimeSaturday;

  TimeOfDay? startTimeSunday;
  TimeOfDay? endTimeSunday;

  // Time Settings
  int timeInterval = 30;
  VisibleStep visibleStep = VisibleStep.thirtieths;

  // Styling Constants
  final double VERTICAL_SPACING_BETWEEN_BUTTONS = 0.025;
  final double HORIZONTAL_SPACING_BETWEEN_DAY_AND_BUTTON = 0.25;
  final double SPACE_BETWEEN_START_AND_END_TIME_BUTTONS = 0.05;
  final double BUTTON_FONT_SIZE = 12;

  bool isLoading = true;

  List doctorAvailabilities = [];

  _selectTime(String dayOfWeek, String startOrEnd) async {
    final TimeOfDay? newTime = await showIntervalTimePicker(
      context: context,
      initialTime: const TimeOfDay(hour: 12, minute: 00),
      interval: timeInterval,
      visibleStep: visibleStep,
    );

    switch (dayOfWeek) {
      case 'Monday':
        setState(() {
          if (startOrEnd == "start") {
            startTimeMonday = newTime;
          } else {
            endTimeMonday = newTime;
          }
        });
        break;
      case 'Tuesday':
        setState(() {
          if (startOrEnd == "start") {
            startTimeTuesday = newTime;
          } else {
            endTimeTuesday = newTime;
          }
        });
        break;
      case 'Wednesday':
        setState(() {
          if (startOrEnd == "start") {
            startTimeWednesday = newTime;
          } else {
            endTimeWednesday = newTime;
          }
        });
        break;
      case 'Thursday':
        setState(() {
          if (startOrEnd == "start") {
            startTimeThursday = newTime;
          } else {
            endTimeThursday = newTime;
          }
        });
        break;
      case 'Friday':
        setState(() {
          if (startOrEnd == "start") {
            startTimeFriday = newTime;
          } else {
            endTimeFriday = newTime;
          }
        });
        break;
      case 'Saturday':
        setState(() {
          if (startOrEnd == "start") {
            startTimeSaturday = newTime;
          } else {
            endTimeSaturday = newTime;
          }
        });
        break;
      case 'Sunday':
        setState(() {
          if (startOrEnd == "start") {
            startTimeSunday = newTime;
          } else {
            endTimeSunday = newTime;
          }
        });
        break;
      default:
        break;
    }

  }

  loadAvailabilities() async {
    setState(() {
      isLoading = true;
    });

    doctorAvailabilities = await DoctorService.fetchDoctorAvailabilities(await getUserIdFromStorage());

    setState(() {
      for (Availability availability in doctorAvailabilities) {
        // monday
        if (availability.dayOfWeek == 1) {
          startTimeMonday = availability.startTime;
          endTimeMonday = availability.endTime;
        } else if (availability.dayOfWeek == 2) {
          startTimeTuesday = availability.startTime;
          endTimeTuesday = availability.endTime;
        } else if (availability.dayOfWeek == 3) {
          startTimeWednesday = availability.startTime;
          endTimeWednesday = availability.endTime;
        } else if (availability.dayOfWeek == 4) {
          startTimeThursday = availability.startTime;
          endTimeThursday = availability.endTime;
        } else if (availability.dayOfWeek == 5) {
          startTimeFriday = availability.startTime;
          endTimeFriday = availability.endTime;
        } else if (availability.dayOfWeek == 6) {
          startTimeSaturday = availability.startTime;
          endTimeSaturday = availability.endTime;
        } else if (availability.dayOfWeek == 7) {
          startTimeSunday = availability.startTime;
          endTimeSunday = availability.endTime;
        }
      }

      isLoading = false;
    });
  }

  updateAvailability() async {

    List weekDaysList = [
      [startTimeMonday, endTimeMonday],
      [startTimeTuesday, endTimeTuesday],
      [startTimeWednesday, endTimeWednesday],
      [startTimeThursday, endTimeThursday],
      [startTimeFriday, endTimeFriday],
      [startTimeSaturday, endTimeSaturday],
      [startTimeSunday, endTimeSunday]
    ];

    for (int i = 0; i < weekDaysList.length; i++) {
      int weekDayIndex = i+1;
      if (weekDaysList[i][0] == null || weekDaysList[i][1] == null) {
      } else {

        if (!mounted) return;

        // Check start time before end time
        if (!(DateTime(2021, 1, 1, weekDaysList[i][0]!.hour, weekDaysList[i][0]!.minute)
            .isBefore(DateTime(2021, 1, 1, weekDaysList[i][1]!.hour, weekDaysList[i][1]!.minute)))) {
          // Show snack bar
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              behavior: SnackBarBehavior.floating,
              margin: const EdgeInsets.only(bottom: 10.0),
              content: Text("Invalid Time Range for ${Utility.convertIntToDayOfWeek(weekDayIndex)}. Check that all start times are before end times"),
              duration: const Duration(seconds: 2),
              backgroundColor: LightPalette.error,
            ),
          );
          return;
        }
      }
    }

    // This time do the operations
    for (int i = 0; i < weekDaysList.length; i++) {
      int weekDayIndex = i+1;
      if (weekDaysList[i][0] == null || weekDaysList[i][1] == null) {
        // DELETE AVAILABILITY IF AVAILABILITY WITH DOCTOR AND DAY EXISTS; ELSE DO NOTHING
        if (doctorAvailabilities.any((availability) => availability.dayOfWeek == weekDayIndex)) {
          await AvailabilityService
              .deleteAvailability(doctorAvailabilities
              .firstWhere((availability) => availability.dayOfWeek == weekDayIndex).availabilityID);
        }
      } else {
        Availability newWeekDayAvailability = Availability(
            availabilityID: -1,
            dayOfWeek: weekDayIndex,
            startTime: weekDaysList[i][0],
            endTime: weekDaysList[i][1],
            doctor: await getUserFromStorage()
        );

        dynamic weekDayResponse = await AvailabilityService
            .createAvailability(newWeekDayAvailability);

        if (!mounted) return;

        if (weekDayResponse == "Invalid Time Range") {
          // Show snack bar
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              behavior: SnackBarBehavior.floating,
              margin: const EdgeInsets.only(bottom: 10.0),
              content: Text("Invalid Time Range for ${Utility.convertIntToDayOfWeek(weekDayIndex)}. Check that all start times are before end times"),
              duration: const Duration(seconds: 2),
              backgroundColor: LightPalette.error,
            ),
          );
          return;
        } else if (weekDayResponse == "Resource Already Exists") {
          // Find availability id with that doctor id and day of week and update that
          await AvailabilityService
              .updateAvailability(doctorAvailabilities
              .firstWhere((availability) => availability.dayOfWeek == weekDayIndex).availabilityID, newWeekDayAvailability);
        }
      }
    }

    if (!mounted) return;

    // If all validated and success
    ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          content: Text("Availability Updated"),
          duration: Duration(seconds: 2),
          backgroundColor: LightPalette.success,
        ),
      );
      Navigator.pop(context);

  }

  @override
  void initState() {
    super.initState();
    loadAvailabilities();
  }

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    Color secondaryThemeColor = Theme.of(context).colorScheme.secondary;

    return Scaffold(
      appBar: AppBar(
        title: const Text("Edit Availability"),
      ),
      body: Builder(
        builder: (context) {
          if (isLoading) {
            return const Center(
              child: CircularProgressIndicator(),
            );
          } else {
            return SizedBox(
              height: height,
              child: SingleChildScrollView(
                child: Container(
                  padding: EdgeInsets.symmetric(horizontal: 0, vertical: height * 0.025),
                  child: Padding(
                    padding: EdgeInsets.fromLTRB(width * 0.05, 0, 0, 0),
                    child: Column(
                      children: [
                        Row(
                            children: [
                              SizedBox(width: width * HORIZONTAL_SPACING_BETWEEN_DAY_AND_BUTTON, child: const Text("Monday:"),),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Monday", "start");
                                },
                                child: Text(
                                  startTimeMonday != null ? startTimeMonday!.format(context) : "Select Start Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                              SizedBox(width: width * SPACE_BETWEEN_START_AND_END_TIME_BUTTONS,),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Monday", "end");
                                },
                                child: Text(
                                  endTimeMonday != null ? endTimeMonday!.format(context) : "Select End Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                            ]
                        ),
                        SizedBox(height: height * VERTICAL_SPACING_BETWEEN_BUTTONS,),
                        Row(
                            children: [
                              SizedBox(width: width * HORIZONTAL_SPACING_BETWEEN_DAY_AND_BUTTON, child: const Text("Tuesday:")),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Tuesday", "start");
                                },
                                child: Text(
                                  startTimeTuesday != null ? startTimeTuesday!.format(context) : "Select Start Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                              SizedBox(width: width * SPACE_BETWEEN_START_AND_END_TIME_BUTTONS,),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Tuesday", "end");
                                },
                                child: Text(
                                  endTimeTuesday != null ? endTimeTuesday!.format(context) : "Select End Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                            ]
                        ),
                        SizedBox(height: height * VERTICAL_SPACING_BETWEEN_BUTTONS,),
                        Row(
                            children: [
                              SizedBox(width: width * HORIZONTAL_SPACING_BETWEEN_DAY_AND_BUTTON, child: const Text("Wednesday:")),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Wednesday", "start");
                                },
                                child: Text(
                                  startTimeWednesday != null ? startTimeWednesday!.format(context) : "Select Start Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                              SizedBox(width: width * SPACE_BETWEEN_START_AND_END_TIME_BUTTONS,),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Wednesday", "end");
                                },
                                child: Text(
                                  endTimeWednesday != null ? endTimeWednesday!.format(context) : "Select End Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                            ]
                        ),
                        SizedBox(height: height * VERTICAL_SPACING_BETWEEN_BUTTONS,),
                        Row(
                            children: [
                              SizedBox(width: width * HORIZONTAL_SPACING_BETWEEN_DAY_AND_BUTTON, child: const Text("Thursday:")),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Thursday", "start");
                                },
                                child: Text(
                                  startTimeThursday != null ? startTimeThursday!.format(context) : "Select Start Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                              SizedBox(width: width * SPACE_BETWEEN_START_AND_END_TIME_BUTTONS,),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Thursday", "end");
                                },
                                child: Text(
                                  endTimeThursday != null ? endTimeThursday!.format(context) : "Select End Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                            ]
                        ),
                        SizedBox(height: height * VERTICAL_SPACING_BETWEEN_BUTTONS,),
                        Row(
                            children: [
                              SizedBox(width: width * HORIZONTAL_SPACING_BETWEEN_DAY_AND_BUTTON, child: const Text("Friday:")),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Friday", "start");
                                },
                                child: Text(
                                  startTimeFriday != null ? startTimeFriday!.format(context) : "Select Start Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                              SizedBox(width: width * SPACE_BETWEEN_START_AND_END_TIME_BUTTONS,),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Friday", "end");
                                },
                                child: Text(
                                  endTimeFriday != null ? endTimeFriday!.format(context) : "Select End Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                            ]
                        ),
                        SizedBox(height: height * VERTICAL_SPACING_BETWEEN_BUTTONS,),
                        Row(
                            children: [
                              SizedBox(width: width * HORIZONTAL_SPACING_BETWEEN_DAY_AND_BUTTON, child: const Text("Saturday:")),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Saturday", "start");
                                },
                                child: Text(
                                  startTimeSaturday != null ? startTimeSaturday!.format(context) : "Select Start Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                              SizedBox(width: width * SPACE_BETWEEN_START_AND_END_TIME_BUTTONS,),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Saturday", "end");
                                },
                                child: Text(
                                  endTimeSaturday != null ? endTimeSaturday!.format(context) : "Select End Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                            ]
                        ),
                        SizedBox(height: height * VERTICAL_SPACING_BETWEEN_BUTTONS,),
                        Row(
                            children: [
                              SizedBox(width: width * HORIZONTAL_SPACING_BETWEEN_DAY_AND_BUTTON, child: const Text("Sunday:")),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Sunday", "start");
                                },
                                child: Text(
                                  startTimeSunday != null ? startTimeSunday!.format(context) : "Select Start Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                              SizedBox(width: width * SPACE_BETWEEN_START_AND_END_TIME_BUTTONS,),
                              ElevatedButton(
                                style: ButtonStyle(
                                  minimumSize: MaterialStateProperty.all<Size>(Size(width * 0.2, height * 0.05)),
                                ),
                                onPressed: () {
                                  _selectTime("Sunday", "end");
                                },
                                child: Text(
                                  endTimeSunday != null ? endTimeSunday!.format(context) : "Select End Time",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: BUTTON_FONT_SIZE),
                                ),
                              ),
                            ]
                        ),
                        SizedBox(height: height * VERTICAL_SPACING_BETWEEN_BUTTONS,),
                        ElevatedButton(
                          onPressed: () async {
                            await updateAvailability();
                          },
                          style: ButtonStyle(
                            backgroundColor: MaterialStateProperty.all<Color>(secondaryThemeColor),
                          ),
                          child: const Text('Save',
                            style: TextStyle(fontWeight: FontWeight.bold),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            );
          }
        }
      ),
    );
  }
}

