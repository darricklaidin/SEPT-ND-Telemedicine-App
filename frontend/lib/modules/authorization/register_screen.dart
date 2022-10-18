import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:intl/intl.dart';

import '../../config/themes/light_palette.dart';
import '../../models/api_response.dart';
import '../../modules/authorization/login_screen.dart';

class RegisterScreen extends StatefulWidget {
  final AuthService authService;

  const RegisterScreen({Key? key, required this.authService}) : super(key: key);

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
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

  @override
  void initState() {
    super.initState();
  }

  Future<void> registration(context) async {
    ApiResponse res = ApiResponse();

    if (dateInput == null) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text(res.msg ?? 'Invalid Date of Birth'),
          backgroundColor: LightPalette.error));
      return;
    }

    try {
      if (_formKey.currentState!.validate()) {
        var firstName = _fnameController.text;
        var lastName = _lnameController.text;
        var email = _emailController.text;
        var password = _passwordController.text;

        res = await widget.authService
            .registerUser(firstName, lastName, email, password, dateInput!);
      }
    } catch (e) {
      res.msg = e.toString();
    }

    if (res.success) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          content: Text("Registration Successful"),
          duration: Duration(seconds: 2),
          backgroundColor: LightPalette.success,
        ),
      );
      Navigator.pushNamed(context, '/home');
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text(res.msg ?? 'Invalid Fields'),
          backgroundColor: LightPalette.error));
    }
  }

  @override
  Widget build(BuildContext context) {
    // var passKey = GlobalKey<FormFieldState>();

    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;

    return Scaffold(
      appBar: AppBar(
        title: const Center(child: Text("Registration")),
      ),
      body: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Padding(
            padding:
                EdgeInsets.symmetric(horizontal: 0, vertical: height * 0.05),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Align(
                  alignment: Alignment.center,
                  child: SizedBox(
                    width: 300,
                    child: TextFormField(
                      key: const Key('first name'),
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
                      key: const Key('last name'),
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
                  child: Container(
                      width: 300,
                      padding: EdgeInsets.symmetric(horizontal: width * 0.05),
                      child: Row(
                        children: [
                          const Text("Date of Birth:"),
                          SizedBox(
                            width: width * 0.15,
                          ),
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
                                String formattedDate =
                                    DateFormat('yyyy-MM-dd').format(pickedDate);
                                setState(() {
                                  dateInput =
                                      formattedDate; //set output date to TextField value.
                                });
                              }
                            },
                            child: Text(
                              dateInput ?? "Select Date",
                              style:
                                  const TextStyle(fontWeight: FontWeight.bold),
                            ),
                          ),
                        ],
                      )),
                ),
                const SizedBox(height: 20),
                Align(
                  alignment: Alignment.center,
                  child: SizedBox(
                    width: 300,
                    child: TextFormField(
                      key: const Key('email'),
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
                    key: const Key('password'),
                    controller: _passwordController,
                    obscureText: true,
                    decoration: const InputDecoration(
                      border: OutlineInputBorder(),
                      labelText: 'Password',
                    ),
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return 'Password cannot be empty';
                      } else if (value.length < 8) {
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
                    key: const Key('confirm password'),
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
                ElevatedButton(
                  style: ButtonStyle(
                    backgroundColor:
                        MaterialStateProperty.all(LightPalette.secondary),
                  ),
                  onPressed: () => registration(context),
                  child: const Text("Register",
                      style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                      )),
                ),
                const SizedBox(height: 20),
                RichText(
                  text: TextSpan(
                    children: [
                      const TextSpan(
                        text: "Already have an account? ",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: "Raleway",
                        ),
                      ),
                      TextSpan(
                        text: 'Login now',
                        style: TextStyle(
                          color: Colors.deepPurple[300],
                          fontFamily: "Raleway",
                        ),
                        recognizer: TapGestureRecognizer()
                          ..onTap = () {
                            Navigator.pushReplacement(
                              context,
                              MaterialPageRoute(
                                  builder: (context) => LoginScreen(
                                      authService: widget.authService)),
                            );
                          },
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
