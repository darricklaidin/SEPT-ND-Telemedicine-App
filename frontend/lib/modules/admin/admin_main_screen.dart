import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:frontend/modules/admin/admin_doctors_screen.dart';
import 'package:frontend/modules/admin/admin_home_screen.dart';
import 'package:frontend/modules/admin/admin_patients_screen.dart';
import 'package:frontend/services/specialty_service.dart';
import 'package:persistent_bottom_nav_bar/persistent-tab-view.dart';

import '../../services/auth_service.dart';

class AdminMainScreen extends StatefulWidget {
  const AdminMainScreen({Key? key}) : super(key: key);

  @override
  State<AdminMainScreen> createState() => _AdminMainScreenState();
}

class _AdminMainScreenState extends State<AdminMainScreen> {
  PersistentTabController? _controller;

  @override
  void initState() {
    super.initState();
    _controller = PersistentTabController(initialIndex: 0);
  }

  @override
  Widget build(BuildContext context) {
    return Builder(builder: (context) {
      return PersistentTabView(
        context,
        controller: _controller,
        screens: _buildScreens(),
        items: _navBarsItems(),
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

  List<Widget> _buildScreens() {
    return [
      AdminHomeScreen(
        authService: AuthService(),
        specialtyService: SpecialtyService(),
      ),
      AdminPatientsScreen(specialtyService: SpecialtyService()),
      AdminDoctorsScreen(specialtyService: SpecialtyService()),
    ];
  }

  List<PersistentBottomNavBarItem> _navBarsItems() {
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
        icon: const Icon(CupertinoIcons.person_3_fill),
        title: ("Patients"),
        activeColorPrimary: CupertinoColors.activeBlue,
        inactiveColorPrimary: CupertinoColors.systemGrey,
        routeAndNavigatorSettings: const RouteAndNavigatorSettings(
          initialRoute: '/patients',
        ),
      ),
      PersistentBottomNavBarItem(
        icon: const Icon(Icons.medical_services_rounded),
        title: ("Doctors"),
        activeColorPrimary: CupertinoColors.activeBlue,
        inactiveColorPrimary: CupertinoColors.systemGrey,
        routeAndNavigatorSettings: const RouteAndNavigatorSettings(
          initialRoute: '/doctors',
        ),
      ),
    ];
  }
}
