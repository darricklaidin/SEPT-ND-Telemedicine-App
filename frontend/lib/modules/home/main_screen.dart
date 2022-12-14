import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:frontend/services/patient_service.dart';
import 'package:persistent_bottom_nav_bar/persistent-tab-view.dart';

import '../../models/patient.dart';
import '../../services/auth_service.dart';
import '../../services/doctor_service.dart';
import '../../services/specialty_service.dart';
import '../appointment/manage_appointments_screen.dart';
import '../notifications/notifications.dart';
import '../search/search_screen.dart';
import 'empty_home_screen.dart';

class MainScreen extends StatefulWidget {
  const MainScreen({Key? key}) : super(key: key);

  @override
  State<MainScreen> createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen> {
  PersistentTabController? _controller;
  AuthService authService = AuthService();
  String userRole = "";
  Patient? patient;

  void _handleTabSelection(int controllerIndex) {
    _controller!.index = controllerIndex;
  }

  void setUser() async {
    userRole = await authService.getUserRoleFromStorage();
    if (userRole == "PATIENT") {
      patient = await authService.getUserFromStorage();
    }
    setState(() {});
  }

  @override
  void initState() {
    super.initState();
    setUser();
    _controller = PersistentTabController(initialIndex: 0);
  }

  @override
  Widget build(BuildContext context) {
    return Builder(builder: (context) {
      return PersistentTabView(
        context,
        controller: _controller,
        screens: userRole == "PATIENT"
            ? _buildPatientScreens()
            : _buildDoctorScreens(),
        items: userRole == "PATIENT"
            ? _patientNavBarsItems()
            : _doctorNavBarsItems(),
        confineInSafeArea: true,
        backgroundColor: Colors.white, // Default is Colors.white.
        handleAndroidBackButtonPress: true, // Default is true.
        resizeToAvoidBottomInset:
            true, // This needs to be true if you want to move up the screen when keyboard appears. Default is true.
        stateManagement: true, // Default is true.
        hideNavigationBarWhenKeyboardShows:
            true, // Recommended to set 'resizeToAvoidBottomInset' as true while using this argument. Default is true.
        decoration: NavBarDecoration(
          borderRadius: BorderRadius.circular(10.0),
          colorBehindNavBar: Colors.white,
        ),
        popAllScreensOnTapOfSelectedTab: true,
        popActionScreens: PopActionScreensType.all,
        itemAnimationProperties: const ItemAnimationProperties(
          // Navigation Bar's items animation properties.
          duration: Duration(milliseconds: 200),
          curve: Curves.ease,
        ),
        screenTransitionAnimation: const ScreenTransitionAnimation(
          // Screen transition animation on change of selected tab.
          animateTabTransition: true,
          curve: Curves.ease,
          duration: Duration(milliseconds: 200),
        ),
        navBarStyle:
            NavBarStyle.style1, // Choose the nav bar style with this property.
      );
    });
  }

  List<Widget> _buildDoctorScreens() {
    return [
      EmptyHomeScreen(
        handleTabSelection: _handleTabSelection,
        authService: AuthService(),
        specialtyService: SpecialtyService(),
      ),
      SearchScreen(
        authService: AuthService(),
        doctorService: DoctorService(),
        specialtyService: SpecialtyService(),
      ),
      ManageAppointmentsScreen(
        handleTabSelection: _handleTabSelection,
        authService: AuthService(),
        patientService: PatientService(),
        doctorService: DoctorService(),
        specialtyService: SpecialtyService(),
      ),
      const NotificationScreen(),
    ];
  }

  List<Widget> _buildPatientScreens() {
    return [
      EmptyHomeScreen(
        handleTabSelection: _handleTabSelection,
        authService: AuthService(),
        specialtyService: SpecialtyService(),
      ),
      SearchScreen(
        authService: AuthService(),
        doctorService: DoctorService(),
        specialtyService: SpecialtyService(),
      ),
      ManageAppointmentsScreen(
        handleTabSelection: _handleTabSelection,
        authService: AuthService(),
        patientService: PatientService(),
        doctorService: DoctorService(),
        specialtyService: SpecialtyService(),
      ),
      const NotificationScreen(),
    ];
  }

  List<PersistentBottomNavBarItem> _patientNavBarsItems() {
    return [
      PersistentBottomNavBarItem(
        icon: const Icon(CupertinoIcons.home),
        title: ("Home"),
        activeColorPrimary: CupertinoColors.activeBlue,
        inactiveColorPrimary: CupertinoColors.systemGrey,
        routeAndNavigatorSettings: const RouteAndNavigatorSettings(
          initialRoute: '/home',
        ),
      ),
      PersistentBottomNavBarItem(
        icon: const Icon(CupertinoIcons.search),
        title: ("Search"),
        activeColorPrimary: CupertinoColors.activeBlue,
        inactiveColorPrimary: CupertinoColors.systemGrey,
        routeAndNavigatorSettings: const RouteAndNavigatorSettings(
          initialRoute: '/search',
        ),
      ),
      PersistentBottomNavBarItem(
        icon: const Icon(CupertinoIcons.calendar),
        title: ("Appointments"),
        activeColorPrimary: CupertinoColors.activeBlue,
        inactiveColorPrimary: CupertinoColors.systemGrey,
        routeAndNavigatorSettings: const RouteAndNavigatorSettings(
          initialRoute: '/appointments',
        ),
      ),
      PersistentBottomNavBarItem(
        icon: const Icon(Icons.medical_information),
        title: ("Prescriptions"),
        activeColorPrimary: CupertinoColors.activeBlue,
        inactiveColorPrimary: CupertinoColors.systemGrey,
        routeAndNavigatorSettings: const RouteAndNavigatorSettings(
          initialRoute: '/prescriptions',
        ),
      ),
      PersistentBottomNavBarItem(
        icon: const Icon(CupertinoIcons.bell),
        title: ("Notifications"),
        activeColorPrimary: CupertinoColors.activeBlue,
        inactiveColorPrimary: CupertinoColors.systemGrey,
        routeAndNavigatorSettings: const RouteAndNavigatorSettings(
          initialRoute: '/notifications',
        ),
      ),
    ];
  }

  List<PersistentBottomNavBarItem> _doctorNavBarsItems() {
    return [
      PersistentBottomNavBarItem(
        icon: const Icon(CupertinoIcons.home),
        title: ("Home"),
        activeColorPrimary: CupertinoColors.activeBlue,
        inactiveColorPrimary: CupertinoColors.systemGrey,
        routeAndNavigatorSettings: const RouteAndNavigatorSettings(
          initialRoute: '/home',
        ),
      ),
      PersistentBottomNavBarItem(
        icon: const Icon(CupertinoIcons.search),
        title: ("Search"),
        activeColorPrimary: CupertinoColors.activeBlue,
        inactiveColorPrimary: CupertinoColors.systemGrey,
        routeAndNavigatorSettings: const RouteAndNavigatorSettings(
          initialRoute: '/search',
        ),
      ),
      PersistentBottomNavBarItem(
        icon: const Icon(CupertinoIcons.calendar),
        title: ("Appointments"),
        activeColorPrimary: CupertinoColors.activeBlue,
        inactiveColorPrimary: CupertinoColors.systemGrey,
        routeAndNavigatorSettings: const RouteAndNavigatorSettings(
          initialRoute: '/appointments',
        ),
      ),
      PersistentBottomNavBarItem(
        icon: const Icon(CupertinoIcons.bell),
        title: ("Notifications"),
        activeColorPrimary: CupertinoColors.activeBlue,
        inactiveColorPrimary: CupertinoColors.systemGrey,
        routeAndNavigatorSettings: const RouteAndNavigatorSettings(
          initialRoute: '/notifications',
        ),
      ),
    ];
  }
}
