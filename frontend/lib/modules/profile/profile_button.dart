import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../setting/setting_screen.dart';

class ProfileButton extends StatelessWidget {
  const ProfileButton({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return IconButton(
      splashRadius: 20.0,
      iconSize: 35.0,
      icon: const Icon(CupertinoIcons.profile_circled),
      onPressed: () {
        Navigator.push(context, MaterialPageRoute(builder: (context) => const SettingsScreen()));
      },
      color: Colors.black,
    );
  }

}
