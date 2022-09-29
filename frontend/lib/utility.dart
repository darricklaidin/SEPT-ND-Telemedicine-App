import 'package:flutter/material.dart';

extension StringExtension on String {
  String capitalize() {
    return "${this[0].toUpperCase()}${this.substring(1).toLowerCase()}";
  }
}

class Utility {
  static String timeToString(TimeOfDay timeOfDay) {
    return "${timeOfDay.hour == 12 ? timeOfDay.hour : timeOfDay.hour % 12}:${timeOfDay.minute == 0 ? "00" : timeOfDay.minute} ${timeOfDay.period == DayPeriod.am ? "AM" : "PM"}";
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

  static String convertIntToDayOfWeek(int dayOfWeekInt) {
    switch (dayOfWeekInt) {
      case 1:
        return "Monday";
      case 2:
        return "Tuesday";
      case 3:
        return "Wednesday";
      case 4:
        return "Thursday";
      case 5:
        return "Friday";
      case 6:
        return "Saturday";
      case 7:
        return "Sunday";
      default:
        return "Invalid Day";
    }
  }
}