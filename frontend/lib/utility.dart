import 'package:flutter/material.dart';

class Utility {
  static String timeToString(TimeOfDay timeOfDay) {
    return "${timeOfDay.hour}:${timeOfDay.minute == 0 ? "00" : timeOfDay.minute} ${timeOfDay.period == DayPeriod.am ? "AM" : "PM"}";
  }

  static int convertDayOfWeekToInt(String dayOfWeek) {
    switch (dayOfWeek.toUpperCase()) {
      case "MONDAY":
        return 1;
      case "TUESDAY":
        return 2;
      case "WEDNESDAY":
        return 3;
      case "THURSDAY":
        return 4;
      case "FRIDAY":
        return 5;
      case "SATURDAY":
        return 6;
      case "SUNDAY":
        return 7;
      default:
        return 0;
    }
  }

}