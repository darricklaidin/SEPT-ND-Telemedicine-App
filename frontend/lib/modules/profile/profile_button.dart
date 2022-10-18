import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../../services/auth_service.dart';
import '../setting/setting_screen.dart';

class ProfileButton extends StatelessWidget {
  final AuthService authService;

  const ProfileButton({Key? key, required this.authService}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return IconButton(
      splashRadius: 20.0,
      iconSize: 35.0,
      icon: const Icon(CupertinoIcons.profile_circled),
      onPressed: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => SettingsScreen(authService: authService),
          ),
        );
      },
      color: Colors.black,
    );
  }
}
