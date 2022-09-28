import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:frontend/services/auth_service.dart';

import '../../config/themes/light_palette.dart';
import '../../models/api_response.dart';


class EditPersonalDetailsDoctorScreen extends StatefulWidget {
  const EditPersonalDetailsDoctorScreen({Key? key}) : super(key: key);

  @override
  State<EditPersonalDetailsDoctorScreen> createState() => _EditPersonalDetailsDoctorScreen();
}

class _EditPersonalDetailsDoctorScreen extends State<EditPersonalDetailsDoctorScreen> {
  final _formKey = GlobalKey<FormState>();

  @override
  void initState() {

    super.initState();
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Center(child: Text("Personal Details")),
        ),
        body: Form(
          key: _formKey,
          child: Center(
            child: SingleChildScrollView(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Align(
                    alignment: Alignment.center,
                    child: SizedBox(
                      width: 300,
                      child: TextFormField(
                        decoration: const InputDecoration(
                          border: OutlineInputBorder(),
                          labelText: 'First Name',
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Email cannot be empty';
                          } else if (!RegExp(
                              r"^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+")
                              .hasMatch(value)) {
                            return 'Invalid email';
                          }
                          return null;
                        },
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  SizedBox(
                    width: 300,
                    child: TextFormField(
                      obscureText: true,
                      decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        labelText: 'Last Name',
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Password cannot be empty';
                        }
                        // FIXME: Uncomment when deploying
                        // else if (value.length < 8) {
                        //   return 'Password should be atleast 8 characters';
                        // }
                        return null;
                      },
                    ),
                  ),
                  const SizedBox(height: 20),
                  SizedBox(
                    width: 300,
                    child: TextFormField(
                      obscureText: true,
                      decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        labelText: 'Mobile No.',
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Password cannot be empty';
                        }
                        // FIXME: Uncomment when deploying
                        // else if (value.length < 8) {
                        //   return 'Password should be atleast 8 characters';
                        // }
                        return null;
                      },
                    ),
                  ),
                  const SizedBox(height: 20),
                  SizedBox(
                    width: 300,
                    child: TextFormField(
                      obscureText: true,
                      decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        labelText: 'Username',
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Password cannot be empty';
                        }
                        // FIXME: Uncomment when deploying
                        // else if (value.length < 8) {
                        //   return 'Password should be atleast 8 characters';
                        // }
                        return null;
                      },
                    ),
                  ),
                  const SizedBox(height: 20),
                  SizedBox(
                    width: 300,
                    child: TextFormField(
                      decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        labelText: 'Email',
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Email cannot be empty';
                        } else if (!RegExp(
                            r"^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+")
                            .hasMatch(value)) {
                          return 'Invalid email';
                        }
                        return null;
                      },
                    ),
                  ),
                  const SizedBox(height: 20),
                  SizedBox(
                    width: 300,
                    child: TextFormField(
                      decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        labelText: 'Specialty',
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Email cannot be empty';
                        } else if (!RegExp(
                            r"^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+")
                            .hasMatch(value)) {
                          return 'Invalid email';
                        }
                        return null;
                      },
                    ),
                  ),
                  const SizedBox(height: 20),
                  ElevatedButton(
                    style: ButtonStyle(
                      backgroundColor: MaterialStateProperty.all(Colors.red),
                    ),
                    onPressed: () => {},
                    child: const Text(
                      "Save",
                    ),
                  ),
                  const SizedBox(height: 20),
                ],
              ),
            ),
          ),
        ));
  }
}