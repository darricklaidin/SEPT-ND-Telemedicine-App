import 'package:flutter/material.dart';
import 'package:frontend/services/patient_service.dart';
import 'package:intl/intl.dart';

import '../../config/themes/light_palette.dart';
import '../../models/api_response.dart';
import '../../models/patient.dart';

class EditPatientPersonalDetailsScreen extends StatefulWidget {
  final int patientID;

  const EditPatientPersonalDetailsScreen({Key? key, required this.patientID,}) : super(key: key);

  @override
  State<EditPatientPersonalDetailsScreen> createState() => _EditPatientPersonalDetailsScreenState();
}

class _EditPatientPersonalDetailsScreenState extends State<EditPatientPersonalDetailsScreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _fnameController =
  TextEditingController(text: '');
  final TextEditingController _lnameController =
  TextEditingController(text: '');
  final TextEditingController _emailController =
  TextEditingController(text: '');
  final TextEditingController _passwordController =
  TextEditingController(text: '');
  final TextEditingController _cpasswordController =
  TextEditingController(text: '');

  String? dateInput;

  Future<void> updateDetails(context) async {
    ApiResponse res = ApiResponse();
    res.msg = "Error";

    if (dateInput == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          content: Text("Invalid Date of Birth"),
          duration: Duration(seconds: 2),
          backgroundColor: LightPalette.error,
        ),
      );
      return;
    }

    try {
      if (_formKey.currentState!.validate()) {
        var firstName = _fnameController.text;
        var lastName = _lnameController.text;
        var email = _emailController.text;
        var password = _passwordController.text;

        Patient updatedPatient = Patient(
          userID: widget.patientID,
          firstName: firstName,
          lastName: lastName,
          email: email,
          dateOfBirth: DateFormat('yyyy-MM-dd').parse(dateInput!),
          accountStatus: true,
          symptoms: null,  // Leave symptoms as null so that it is not updated as '' (blank)
        );

        res.msg = await PatientService.updatePatient(widget.patientID, updatedPatient, password);
        if (res.msg == "Success") {
          res.success = true;
        }
      }
    } catch (e) {
      res.msg = e.toString();
    }

    if (res.success) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          content: Text("Personal details updated successfully."),
          duration: Duration(seconds: 2),
          backgroundColor: LightPalette.success,
        ),
      );
      Navigator.pop(context);
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: const EdgeInsets.only(bottom: 10.0),
          content: Text(res.msg!),
          duration: const Duration(seconds: 2),
          backgroundColor: LightPalette.error,
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    // var passKey = GlobalKey<FormFieldState>();

    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;

    return Scaffold(
        appBar: AppBar(
          title: const Center(child: Text("Update Personal Details")),
        ),
        body: Form(
          key: _formKey,
          child: SingleChildScrollView(
            child: Padding(
              padding: EdgeInsets.symmetric(horizontal: 0, vertical: height * 0.05),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Align(
                    alignment: Alignment.center,
                    child: SizedBox(
                      width: 300,
                      child: TextFormField(
                        controller: _fnameController,
                        decoration: const InputDecoration(
                          border: OutlineInputBorder(),
                          labelText: 'First Name',
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'First name cannot be empty';
                          }
                          return null;
                        },
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  Align(
                    alignment: Alignment.center,
                    child: SizedBox(
                      width: 300,
                      child: TextFormField(
                        controller: _lnameController,
                        decoration: const InputDecoration(
                          border: OutlineInputBorder(),
                          labelText: 'Last Name',
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Last name cannot be empty';
                          }
                          return null;
                        },
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  Align(
                    alignment: Alignment.center,
                    child: SizedBox(
                      width: 300,
                      child: TextFormField(
                        controller: _emailController,
                        decoration: const InputDecoration(
                          border: OutlineInputBorder(),
                          labelText: 'E-mail',
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
                      controller: _passwordController,
                      // key: passKey,
                      obscureText: true,
                      decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        labelText: 'Password',
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return 'Password cannot be empty';
                        }
                        else if (value.length < 8) {
                          return 'Password should be at least 8 characters';
                        }
                        return null;
                      },
                    ),
                  ),
                  const SizedBox(height: 20),
                  SizedBox(
                    width: 300,
                    child: TextFormField(
                      controller: _cpasswordController,
                      obscureText: true,
                      decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        labelText: 'Confirm Password',
                      ),
                      validator: (value) {
                        // var password = passKey.currentState.value;
                        if (value != _passwordController.text) {
                          return 'Passwords must match';
                        }
                        return null;
                      },
                    ),
                  ),
                  const SizedBox(height: 20),
                  Align(
                    alignment: Alignment.center,
                    child: Container(
                        width: 300,
                        padding: EdgeInsets.symmetric(horizontal: width * 0.05),
                        child: Row(
                          children: [
                            const Text("Date of Birth:"),
                            SizedBox(width: width * 0.15,),
                            ElevatedButton(
                              onPressed: () async {
                                DateTime? pickedDate = await showDatePicker(
                                  context: context,
                                  initialDate: DateTime.now(),
                                  firstDate: DateTime(1900),
                                  //DateTime.now() - not to allow to choose before today.
                                  lastDate: DateTime.now(),
                                );

                                if (pickedDate != null) {
                                  String formattedDate = DateFormat('yyyy-MM-dd').format(pickedDate);
                                  setState(() {
                                    dateInput = formattedDate; //set output date to TextField value.
                                  });
                                }
                              },
                              child: Text(dateInput ?? "Select Date",
                                style: const TextStyle(fontWeight: FontWeight.bold),
                              ),
                            ),
                          ],
                        )
                    ),
                  ),
                  const SizedBox(height: 20),
                  ElevatedButton(
                    style: ButtonStyle(
                      backgroundColor: MaterialStateProperty.all(LightPalette.secondary),
                    ),
                    onPressed: () => updateDetails(context),
                    child: const Text("Update",
                        style: TextStyle(
                          color: Colors.white,
                          fontWeight: FontWeight.bold,
                        )
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