import 'dart:async';

import 'package:flutter/material.dart';
import 'package:frontend/services/doctor_service.dart';
import 'package:persistent_bottom_nav_bar/persistent-tab-view.dart';

import 'package:frontend/modules/admin/admin_view_user_profile_screen.dart';

import '../../services/specialty_service.dart';

class AdminDoctorsScreen extends StatefulWidget {
  final SpecialtyService specialtyService;

  const AdminDoctorsScreen({Key? key, required this.specialtyService})
      : super(key: key);

  @override
  State<AdminDoctorsScreen> createState() => _AdminDoctorsScreenState();
}

class _AdminDoctorsScreenState extends State<AdminDoctorsScreen> {
  late List allUsers;
  List suggestions = [];
  String searchText = "";
  bool isLoading = true;
  bool timeUp = false;

  Future loadUsers() async {
    List? users;

    setState(() {
      isLoading = true;
      timeUp = false;
    });

    try {
      users = await DoctorService.fetchAllDoctors();
    } on TimeoutException {
      setState(() {
        timeUp = true;
        isLoading = false;
      });
      return;
    } on Exception catch (exception) {
      print(exception);
      return;
    }

    setState(() {
      allUsers = users!;
      suggestions = List.from(allUsers);
      isLoading = false;
    });
  }

  void filter(String value) {
    suggestions = allUsers.where((user) {
      final String fullName = '${user.firstName} ${user.lastName}';
      return fullName.toLowerCase().contains(value.toLowerCase());
    }).toList();
    setState(() => searchText = value);
  }

  @override
  void initState() {
    super.initState();
    // Fetch all users from database
    loadUsers();
  }

  @override
  Widget build(BuildContext context) {
    double height = MediaQuery.of(context).size.height;
    double width = MediaQuery.of(context).size.width;
    Color primaryThemeColor = Theme.of(context).colorScheme.primary;
    Color secondaryThemeColor = Theme.of(context).colorScheme.secondary;
    Color errorThemeColor = Theme.of(context).errorColor;

    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        backgroundColor: Colors.transparent,
      ),
      body: Padding(
        padding: EdgeInsets.fromLTRB(width * 0.05, 0, width * 0.05, 0),
        child: Column(
          children: [
            // Search bar
            TextField(
              decoration: InputDecoration(
                hintText: 'Search for doctors',
                prefixIcon: const Icon(Icons.search),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(20),
                ),
              ),
              onChanged: (value) {
                filter(value);
              },
            ),
            // Number of results found
            Align(
              alignment: Alignment.topLeft,
              child: Container(
                margin: EdgeInsets.fromLTRB(width * 0.05, height * 0.01, 0, 0),
                child: Text(
                  '${suggestions.length} results found',
                  style: const TextStyle(
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),
            SizedBox(
              height: height * 0.03,
            ),
            // List of results
            Builder(builder: (context) {
              if (isLoading) {
                return const Center(child: CircularProgressIndicator());
              } else if (timeUp) {
                return Expanded(
                  child: RefreshIndicator(
                    onRefresh: () async {
                      await loadUsers();
                      filter(searchText);
                    },
                    child: ListView(children: const [
                      Center(
                        child: Text("Timeout: Unable to fetch doctors"),
                      ),
                    ]),
                  ),
                );
              } else if (suggestions.isEmpty) {
                return Expanded(
                  child: RefreshIndicator(
                    onRefresh: () async {
                      await loadUsers();
                      filter(searchText);
                    },
                    child: ListView(children: const [
                      Center(
                        child: Text('No results found'),
                      ),
                    ]),
                  ),
                );
              }
              return Expanded(
                  child: RefreshIndicator(
                onRefresh: () async {
                  await loadUsers();
                  filter(searchText);
                },
                child: ListView.separated(
                  padding: EdgeInsets.zero,
                  itemCount: suggestions.length,
                  itemBuilder: (context, index) {
                    return ListTile(
                      tileColor: primaryThemeColor,
                      title: Text(
                        '${suggestions[index].firstName} ${suggestions[index].lastName}',
                        style: const TextStyle(
                          color: Colors.white,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      subtitle: Text(
                        "Account Status: ${suggestions[index].accountStatus ? 'Active' : 'Inactive'}",
                        style: const TextStyle(color: Colors.white),
                      ),
                      onTap: () async {
                        // Before navigating to the user profile screen, check
                        // if the user still exists in the database
                        if (await DoctorService.fetchDoctor(
                                suggestions[index].userID) ==
                            "Resource Not Found") {
                          ScaffoldMessenger.of(context).showSnackBar(
                            SnackBar(
                              behavior: SnackBarBehavior.floating,
                              margin: const EdgeInsets.only(bottom: 10.0),
                              duration: const Duration(seconds: 2),
                              content: const Text('User no longer exists'),
                              backgroundColor: errorThemeColor,
                            ),
                          );
                          await loadUsers();
                          filter(searchText);
                        } else {
                          pushNewScreen(
                            context,
                            screen: AdminViewUserProfileScreen(
                              user: suggestions[index],
                              userRole: "DOCTOR",
                              specialtyService: widget.specialtyService,
                            ),
                          );
                        }
                      },
                      focusColor: Colors.white,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10),
                      ),
                    );
                  },
                  separatorBuilder: (context, index) {
                    return SizedBox(height: height * 0.02);
                  },
                ),
              ));
            })
          ],
        ),
      ),
    );
  }
}
