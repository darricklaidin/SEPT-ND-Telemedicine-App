import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({Key? key}) : super(key: key);

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
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
          const Align(
            alignment: Alignment.center,
            child: SizedBox(
              width: 300,
              child: TextField(
                decoration: InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: 'E-mail',
                ),
              ),
            ),
          ),
          const SizedBox(height: 20),
          const SizedBox(
            width: 300,
            child: TextField(
              obscureText: true,
              decoration: InputDecoration(
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
            onPressed: () {},
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
