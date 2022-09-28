import 'package:flutter/material.dart';
import 'package:frontend/modules/admin/admin_set_up_doctor_account.dart';
import 'package:google_fonts/google_fonts.dart';

import 'config/themes/light_palette.dart';
import 'modules/admin/admin_all_doctors.dart';
import 'modules/admin/admin_all_patients.dart';
import 'modules/admin/admin_home.dart';
import 'modules/authorization/login_screen.dart';
import 'modules/home/main_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'SEPT-ND-Telemedicine-App',
        theme: ThemeData(
          colorScheme: ColorScheme.fromSwatch().copyWith(
            primary: LightPalette.primary,
            secondary: LightPalette.secondary,
          ),
          fontFamily: GoogleFonts.raleway().fontFamily, // set default font
        ),
        initialRoute: '/',
        routes: {
          // "/": (context) => const LoginScreen(),
          "/": (context) => AdminAllPatientsScreen(),
          "/home": (context) => const MainScreen()
        });
  }
}
