import 'dart:async';

import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:frontend/modules/authorization/register_screen.dart';
import 'package:frontend/services/auth_service.dart';

import '../../config/themes/light_palette.dart';
import '../../models/api_response.dart';

class LoginScreen extends StatefulWidget {
  final AuthService authService;

  const LoginScreen({Key? key, required this.authService}) : super(key: key);

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _emailController =
      TextEditingController(text: '');
  final TextEditingController _passwordController =
      TextEditingController(text: "");

  bool isLoading = true;

  @override
  void initState() {
    _getAuth();
    super.initState();
  }

  _getAuth() async {
    try {
      // Check if token already exists in storage
      if (await widget.authService.checkAuth() != null) {
        // Check that user account is active
        var user = await widget.authService.getUserFromStorage();

        if (user == null) {
          setState(() {
            isLoading = false;
          });

          if (!mounted) return;

          ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
              content: Text("User no longer exists."),
              backgroundColor: LightPalette.error));

          return;
        }

        if (user != "ADMIN") {
          if (!mounted) return;

          if (user.accountStatus == false) {
            setState(() {
              isLoading = false;
            });
            ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
                content: Text(
                    "Account has been deactivated. Please contact admin for assistance."),
                backgroundColor: LightPalette.error));
            await widget.authService.logoutUser();
            return;
          }
        }

        if (!mounted) return;

        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            behavior: SnackBarBehavior.floating,
            margin: EdgeInsets.only(bottom: 10.0),
            content: Text("Login Successful"),
            duration: Duration(seconds: 2),
            backgroundColor: LightPalette.success,
          ),
        );
        if (await widget.authService.getUserRoleFromStorage() == "ADMIN") {
          if (!mounted) return;
          Navigator.pushReplacementNamed(context, '/admin');
        } else {
          if (!mounted) return;
          Navigator.pushReplacementNamed(context, '/home');
        }
      }
    } on TimeoutException {
      setState(() {
        isLoading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Timeout: Unable to authenticate"),
          backgroundColor: LightPalette.error));
      return;
    }

    setState(() {
      isLoading = false;
    });
  }

  Future<void> login(context) async {
    ApiResponse res = ApiResponse();
    try {
      if (_formKey.currentState!.validate()) {
        var email = _emailController.text;
        var password = _passwordController.text;

        res = await widget.authService.loginUser(email, password);
      }
    } on TimeoutException {
      setState(() {
        isLoading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Timeout: Unable to authenticate"),
          backgroundColor: LightPalette.error));
      return;
    } on Exception {
      res.msg = "Error";
    }

    if (res.msg == "User is disabled") {
      setState(() {
        isLoading = false;
      });

      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text(
              "Account has been deactivated. Please contact admin for assistance."),
          backgroundColor: LightPalette.error));
      return;
    }

    if (res.success) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          behavior: SnackBarBehavior.floating,
          margin: EdgeInsets.only(bottom: 10.0),
          content: Text("Login Successful"),
          duration: Duration(seconds: 2),
          backgroundColor: LightPalette.success,
        ),
      );

      if (await widget.authService.getUserRoleFromStorage() == "ADMIN") {
        await Navigator.pushReplacementNamed(context, '/admin');
      } else {
        await Navigator.pushReplacementNamed(context, '/home');
      }
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text(res.msg ?? 'Invalid Credentials'),
          backgroundColor: LightPalette.error));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Center(child: Text("Login")),
      ),
      body: Builder(
        builder: (context) => isLoading
            ? const Center(child: CircularProgressIndicator())
            : Form(
                key: _formKey,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
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
                            return 'Password should be atleast 8 characters';
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
                      onPressed: () => login(context),
                      child: const Text("Login",
                          style: TextStyle(fontWeight: FontWeight.bold)),
                    ),
                    const SizedBox(height: 20),
                    RichText(
                      text: TextSpan(
                        children: [
                          const TextSpan(
                            text: "Don't have an account? ",
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: "Raleway",
                            ),
                          ),
                          TextSpan(
                            text: 'Register now',
                            style: TextStyle(
                              color: Colors.deepPurple[300],
                              fontFamily: "Raleway",
                            ),
                            recognizer: TapGestureRecognizer()
                              ..onTap = () {
                                Navigator.pushReplacement(
                                    context,
                                    MaterialPageRoute(
                                        builder: (context) => RegisterScreen(
                                            authService: widget.authService)));
                              },
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
      ),
    );
  }
}
