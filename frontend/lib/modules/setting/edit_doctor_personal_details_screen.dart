import 'package:flutter/material.dart';
import 'package:frontend/services/specialty_service.dart';
import 'package:intl/intl.dart';

import '../../config/themes/light_palette.dart';
import '../../models/api_response.dart';
import '../../models/doctor.dart';
import '../../models/specialty.dart';
import '../../services/doctor_service.dart';

class EditDoctorPersonalDetailsScreen extends StatefulWidget {
  final int doctorID;

  const EditDoctorPersonalDetailsScreen({Key? key, required this.doctorID,}) : super(key: key);

  @override
  State<EditDoctorPersonalDetailsScreen> createState() => _EditDoctorPersonalDetailsScreenState();
}

class _EditDoctorPersonalDetailsScreenState extends State<EditDoctorPersonalDetailsScreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _fnameController =
  TextEditingController(text: 'Make');
  final TextEditingController _lnameController =
  TextEditingController(text: 'Dog');
  final TextEditingController _emailController =
  TextEditingController(text: 'mk@g.com');
  final TextEditingController _specialtyController =
  TextEditingController(text: 'Surgery');

  String? dateInput;

  @override
  void initState() {
    super.initState();
  }

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
        var specialty = _specialtyController.text;

        List existingSpecialties = await SpecialtyService.getSpecialties();

        bool specialtyExists = false;
        int specialtyID = -1;

        for (var existingSpecialty in existingSpecialties) {
          if (specialty.toLowerCase() == existingSpecialty.specialtyName.toLowerCase()) {
            specialtyExists = true;
            // Get the id of the existing specialty
            // Update the doctor's specialty with this id
            specialtyID = existingSpecialty.specialtyID;
          }
        }

        // If specialty with that name does not exist
        if (!specialtyExists) {
          // Create new specialty
          Specialty newSpecialty = await SpecialtyService.createSpecialty(Specialty(specialtyID: -1, specialtyName: specialty));
          specialtyID = newSpecialty.specialtyID;
        }

        Doctor updatedDoctor = Doctor(
          userID: widget.doctorID,
          firstName: firstName,
          lastName: lastName,
          email: email,
          dateOfBirth: DateFormat('yyyy-MM-dd').parse(dateInput!),
          specialty: specialty,
        );

        res.msg = await DoctorService.updateDoctor(widget.doctorID, updatedDoctor, null, specialtyID);
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
          title: const Center(child: Text("Update Patient Personal Details")),
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
                  Align(
                    alignment: Alignment.center,
                    child: SizedBox(
                      width: 300,
                      child: TextFormField(
                        controller: _specialtyController,
                        decoration: const InputDecoration(
                          border: OutlineInputBorder(),
                          labelText: 'Specialty',
                        ),
                        validator: (value) {
                          if (value == null || value.isEmpty) {
                            return 'Specialty cannot be empty';
                          }
                          return null;
                        },
                      ),
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