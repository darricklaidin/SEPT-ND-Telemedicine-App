import 'dart:async';

import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:frontend/services/auth_service.dart';

import '../../config/themes/light_palette.dart';
import '../../models/api_response.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({Key? key}) : super(key: key);

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _emailController =
      TextEditingController(text: 'n@g.com');
  final TextEditingController _passwordController =
      TextEditingController(text: "nim@nim123");

  bool isLoading = true;

  @override
  void initState() {
    _getAuth();
    super.initState();
  }

  _getAuth() async {
    // Check if token already exists in storage
    try {
      if (await checkAuth() != null) {
        await Navigator.pushReplacementNamed(context, '/home');
      }
    } on TimeoutException{
      setState(() {
        isLoading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text("Timeout: Unable to authenticate"),
          backgroundColor: LightPalette.error));
      return;
    }

    if (mounted) {
      setState(() {
        isLoading = false;
      });
    }
  }

  Future<void> login(context) async {
    ApiResponse res = ApiResponse();
    try {
      if (_formKey.currentState!.validate()) {
        var email = _emailController.text;
        var password = _passwordController.text;

        res = await loginUser(email, password);
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

    if (res.success) {
      Navigator.pushReplacementNamed(context, '/home');
    } else {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
          content: Text(res.msg ?? 'Invalid Credentials'),
          backgroundColor: LightPalette.error));
    }
  }

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;

    return Scaffold(
      appBar: AppBar(
        title: const Center(child: Text("Login")),
      ),
      body: Builder(
        builder: (context) => isLoading ?
        const Center(child: CircularProgressIndicator()) :
        Form(key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
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
                  obscureText: true,
                  decoration: const InputDecoration(
                    border: OutlineInputBorder(),
                    labelText: 'Password',
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
              ElevatedButton(
                style: ButtonStyle(
                  backgroundColor: MaterialStateProperty.all(LightPalette.secondary),
                ),
                onPressed: () => login(context),
                child: const Text("Login", style: TextStyle(fontWeight: FontWeight.bold)),
              ),
              const SizedBox(height: 20),
              const Text(
                "Forgot Password?",
                style: TextStyle(decoration: TextDecoration.underline),
              ),
              const SizedBox(height: 20),
              RichText(
                text: TextSpan(children: [
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
                          ScaffoldMessenger.of(context)
                              .showSnackBar(const SnackBar(content: Text('Register Text Clicked'),));
                        }),
                  ]),
              )
            ],
          ),
        )
      )
    );
  }

}
