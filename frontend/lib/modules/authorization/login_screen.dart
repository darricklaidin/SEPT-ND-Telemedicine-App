import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:frontend/services/auth_service.dart';

import '../../models/api_response.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({Key? key}) : super(key: key);

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final TextEditingController _emailController =
      TextEditingController(text: 'n@g.com');
  final TextEditingController _passwordController =
      TextEditingController(text: "nim@nim123");

  Future<void> login(context) async {
    var email = _emailController.text;
    var password = _passwordController.text;

    ApiResponse res = await loginUser(email, password);
    if (res.success) {
      Navigator.of(context)
          .pushNamedAndRemoveUntil('/', (Route<dynamic> route) => false);
    } else {
      print(res.msg);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Center(child: Text("Login")),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Align(
            alignment: Alignment.center,
            child: SizedBox(
              width: 300,
              child: TextField(
                controller: _emailController,
                decoration: const InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: 'E-mail',
                ),
              ),
            ),
          ),
          const SizedBox(height: 20),
          SizedBox(
            width: 300,
            child: TextField(
              controller: _passwordController,
              obscureText: true,
              decoration: const InputDecoration(
                border: OutlineInputBorder(),
                labelText: 'Password',
              ),
            ),
          ),
          const SizedBox(height: 20),
          ElevatedButton(
            style: ButtonStyle(
              backgroundColor: MaterialStateProperty.all(Colors.red),
            ),
            onPressed: () => login(context),
            child: const Text(
              "Login",
            ),
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
                      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
                        content: Text('Register Text Clicked'),
                      ));
                    }),
            ]),
          ),
        ],
      ),
    );
  }
}
