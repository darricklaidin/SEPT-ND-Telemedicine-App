import 'package:flutter/material.dart';

class Utility {
  static String timeToString(TimeOfDay timeOfDay) {
    // return "${timeOfDay.hour}:${timeOfDay.minute == 0 ? "00" : timeOfDay.minute}";
    return "${timeOfDay.hour}:${timeOfDay.minute == 0 ? "00" : timeOfDay.minute} ${timeOfDay.period == DayPeriod.am ? "AM" : "PM"}";
  }
}