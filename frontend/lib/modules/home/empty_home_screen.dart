import 'package:flutter/material.dart';
import 'package:auto_size_text/auto_size_text.dart';
import 'package:frontend/main.dart';
import 'package:frontend/models/user.dart';
import '../../services/auth_service.dart';
import '../profile/profile_button.dart';
import 'package:frontend/config/constants.dart';

class EmptyHomeScreen extends StatefulWidget {
  final Function handleTabSelection;

  const EmptyHomeScreen({Key? key, required this.handleTabSelection})
      : super(key: key);

  @override
  State<EmptyHomeScreen> createState() => _EmptyHomeScreenState();
}

class _EmptyHomeScreenState extends State<EmptyHomeScreen> {
  User? user;
  bool isLoading = true;

  Future<void> logout(context) async {
    await AuthService.logoutUser();
    // Pop screen and push login screen
    Navigator.of(context, rootNavigator: true).pushReplacement(
        MaterialPageRoute(builder: (context) => const MyApp()));
  }

  Future loadUser() async {
    setState(() {
      isLoading = true;
    });

    user = await AuthService.getUserFromStorage();

    if (user == null) {
      await AuthService.logoutUser();

      if (!mounted) return;

      Navigator.of(context, rootNavigator: true).pushReplacement(
          MaterialPageRoute(builder: (context) => const MyApp()));
    }

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
    Color primaryThemeColor = Theme.of(context).colorScheme.primary;

    if (isLoading) {
      return const Center(child: CircularProgressIndicator());
    }
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        title: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: const [
            AutoSizeText(
              "Telemed+",
              style: TextStyle(
                  color: Colors.black,
                  fontSize: 30,
                  fontWeight: FontWeight.bold),
            ),
            ProfileButton(),
            // SizedBox(
            //   width: 20,
            // )
          ],
        ),
        automaticallyImplyLeading: false,
      ),
      body: Builder(builder: (context) {
        return SafeArea(
          child: Scaffold(
            body: RefreshIndicator(
              onRefresh: () async {
                await loadUser();
              },
              child: Column(children: [
                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(15, 0, 15, 0),
                    child: Column(
                      children: [
                        const Spacer(),
                        Container(
                          height: (MediaQuery.of(context).size.height * 0.3),
                          width: double.infinity,
                          decoration: const BoxDecoration(
                            image: DecorationImage(
                              image: AssetImage("assets/telemedBck.jpeg"),
                              fit: BoxFit.cover,
                            ),
                          ),
                          child: Padding(
                            padding: const EdgeInsets.fromLTRB(15, 10, 15, 0),
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                const Spacer(),
                                const AutoSizeText(
                                    "The right care when you need it most.",
                                    style: TextStyle(
                                        color: Colors.black,
                                        fontSize: 28,
                                        fontWeight: FontWeight.bold)),
                                const Spacer(),
                                AutoSizeText(
                                    "Talk to a doctor, therapist or medical expert anywhere you are by phone or video.",
                                    style: TextStyle(
                                      color: primaryThemeColor,
                                      fontSize: 12,
                                    )),
                                ElevatedButton(
                                  onPressed: () => {
                                    widget.handleTabSelection(searchPageIndex)
                                  },
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: primaryThemeColor,
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(10.0),
                                    ),
                                  ),
                                  child: const Text(
                                    "Get started now",
                                    style: TextStyle(
                                      color: Colors.white,
                                    ),
                                  ),
                                ),
                                TextButton(
                                  style: TextButton.styleFrom(
                                    textStyle: TextStyle(
                                        fontSize: 10, color: primaryThemeColor),
                                  ),
                                  onPressed: () {},
                                  child: Row(
                                    children: const [
                                      AutoSizeText('How Teledoc works'),
                                      Icon(
                                        Icons.arrow_forward,
                                        size: 10,
                                      )
                                    ],
                                  ),
                                ),
                                const Spacer(),
                              ],
                            ),
                          ),
                        ),
                        const Spacer(),
                        const AutoSizeText(
                          'Categories',
                          style: TextStyle(
                              fontSize: 22,
                              color: Colors.black87,
                              fontWeight: FontWeight.bold),
                        ),
                        const SizedBox(height: 15),
                        SingleChildScrollView(
                          child: GridView.count(
                            childAspectRatio:
                                (MediaQuery.of(context).size.width * 0.1) /
                                    (MediaQuery.of(context).size.height * 0.05),
                            mainAxisSpacing: 5,
                            crossAxisSpacing: 5,
                            shrinkWrap: true,
                            crossAxisCount: 3,
                            children: <Widget>[
                              GestureDetector(
                                onTap: () {},
                                child: Container(
                                  decoration: BoxDecoration(
                                    color: Colors.teal[100],
                                    borderRadius: const BorderRadius.all(
                                      Radius.circular(20.0),
                                    ),
                                  ),
                                  padding: const EdgeInsets.all(20),
                                  child: Column(
                                    children: const [
                                      Spacer(),
                                      Icon(
                                        Icons.health_and_safety_sharp,
                                        size: 40,
                                      ),
                                      AutoSizeText("General",
                                          style: TextStyle(
                                              fontSize: 14,
                                              color: Colors.black)),
                                      Spacer(),
                                    ],
                                  ),
                                ),
                              ),
                              GestureDetector(
                                onTap: () {},
                                child: Container(
                                  decoration: BoxDecoration(
                                    color: Colors.teal[100],
                                    borderRadius: const BorderRadius.all(
                                      Radius.circular(20.0),
                                    ),
                                  ),
                                  padding: const EdgeInsets.all(20),
                                  child: Column(
                                    children: const [
                                      Spacer(),
                                      Icon(
                                        Icons.health_and_safety_sharp,
                                        size: 40,
                                      ),
                                      AutoSizeText("Dental",
                                          style: TextStyle(
                                              fontSize: 14,
                                              color: Colors.black)),
                                      Spacer(),
                                    ],
                                  ),
                                ),
                              ),
                              GestureDetector(
                                onTap: () {},
                                child: Container(
                                  decoration: BoxDecoration(
                                    color: Colors.teal[100],
                                    borderRadius: const BorderRadius.all(
                                      Radius.circular(20.0),
                                    ),
                                  ),
                                  padding: const EdgeInsets.all(20),
                                  child: Column(
                                    children: const [
                                      Spacer(),
                                      Icon(
                                        Icons.health_and_safety_sharp,
                                        size: 40,
                                      ),
                                      AutoSizeText("Cardiac",
                                          style: TextStyle(
                                              fontSize: 14,
                                              color: Colors.black)),
                                      Spacer(),
                                    ],
                                  ),
                                ),
                              ),
                              Container(
                                decoration: BoxDecoration(
                                  color: Colors.teal[100],
                                  borderRadius: const BorderRadius.all(
                                    Radius.circular(20.0),
                                  ),
                                ),
                                padding: const EdgeInsets.all(20),
                                child: Column(
                                  children: const [
                                    Spacer(),
                                    Icon(
                                      Icons.health_and_safety_sharp,
                                      size: 40,
                                    ),
                                    AutoSizeText("Nutrition",
                                        style: TextStyle(
                                            fontSize: 14, color: Colors.black)),
                                    Spacer(),
                                  ],
                                ),
                              ),
                              GestureDetector(
                                onTap: () {},
                                child: Container(
                                  decoration: BoxDecoration(
                                    color: Colors.teal[100],
                                    borderRadius: const BorderRadius.all(
                                      Radius.circular(20.0),
                                    ),
                                  ),
                                  padding: const EdgeInsets.all(20),
                                  child: Column(
                                    children: const [
                                      Spacer(),
                                      Icon(
                                        Icons.health_and_safety_sharp,
                                        size: 40,
                                      ),
                                      AutoSizeText("Eye",
                                          style: TextStyle(
                                              fontSize: 14,
                                              color: Colors.black)),
                                      Spacer(),
                                    ],
                                  ),
                                ),
                              ),
                              GestureDetector(
                                onTap: () {},
                                child: Container(
                                  decoration: BoxDecoration(
                                    color: Colors.teal[100],
                                    borderRadius: const BorderRadius.all(
                                      Radius.circular(20.0),
                                    ),
                                  ),
                                  padding: const EdgeInsets.all(20),
                                  child: Column(
                                    children: const [
                                      Spacer(),
                                      Icon(
                                        Icons.health_and_safety_sharp,
                                        size: 40,
                                      ),
                                      AutoSizeText("Covid-19",
                                          style: TextStyle(
                                              fontSize: 14,
                                              color: Colors.black)),
                                      Spacer(),
                                    ],
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                        const Spacer(),
                      ],
                    ),
                  ),
                )
              ]),
            ),
          ),
        );
      }),
    );
  }
}
