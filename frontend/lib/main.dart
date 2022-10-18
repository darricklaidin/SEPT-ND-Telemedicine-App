import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';

import 'config/themes/light_palette.dart';
import 'modules/admin/admin_main_screen.dart';
import 'modules/authorization/login_screen.dart';
import 'modules/home/main_screen.dart';
import 'services/auth_service.dart';

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
          "/": (context) => LoginScreen(authService: AuthService()),
          "/home": (context) => const MainScreen(),
          "/admin": (context) => const AdminMainScreen(),
        });
  }
}
