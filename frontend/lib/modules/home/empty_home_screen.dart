import 'package:flutter/material.dart';

import 'package:frontend/main.dart';
import 'package:frontend/models/user.dart';
import '../../services/auth_service.dart';

class EmptyHomeScreen extends StatefulWidget {
  const EmptyHomeScreen({Key? key}) : super(key: key);

  @override
  State<EmptyHomeScreen> createState() => _EmptyHomeScreenState();
}

class _EmptyHomeScreenState extends State<EmptyHomeScreen> {

  User? user;
  bool isLoading = true;

  Future<void> logout(context) async {
    await logoutUser();
    // Pop screen and push login screen
    Navigator.of(context, rootNavigator: true).pushReplacement(
        MaterialPageRoute(builder: (context) => const MyApp())
    );
  }

  Future loadUser() async {
    user = await getUserFromStorage();
    setState(() {
      isLoading = false;
    });
  }

  @override
  void initState() {
    super.initState();
    loadUser();
  }

  @override
  Widget build(BuildContext context) {
    double height = MediaQuery.of(context).size.height;
    double width = MediaQuery.of(context).size.width;
    Color themeColor = Theme.of(context).colorScheme.primary;

    return Builder(builder: (context) {
      if (isLoading) {
        return const Center(child: CircularProgressIndicator());
      } else {
        return Scaffold(
          body: Stack(
            alignment: Alignment.center,
            children: [
              Container(
                height: height,
                width: width,
                color: Colors.white,
              ),
              Positioned(
                child: Text("Hello ${user?.firstName}"),
              ),
              Positioned(
                bottom: height * 0.35,
                child: ElevatedButton(
                  onPressed: () => logout(context),
                  style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(Colors.amber),
                    ),
                  child: const Text("Logout"),
                ),
              ),
            ],
          ),
        );
      }
    });
  }
}
