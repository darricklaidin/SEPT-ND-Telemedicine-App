import 'package:flutter/material.dart';

/// Displays chat UI
class ChatView extends StatelessWidget {
  const ChatView({Key? key}) : super(key: key);

  static const routeName = '/';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        title: Row(
          children: const [
            Icon(
              Icons.person,
              color: Colors.grey,
            ),
            Text(
              'Dr. Bellamy Nicholas',
              style: TextStyle(color: Colors.grey),
            ),
          ],
        ),
        leading: Padding(
          padding: const EdgeInsets.all(4),
          child: OutlinedButton(
            child: const Icon(
              Icons.arrow_back,
              color: Colors.black,
              size: 22,
            ),
            onPressed: () {},
          ),
        ),
      ),
      body: Column(
        children: [
          Row(
            // crossAxisAlignment: CrossAxisAlignment.stretch,
            // mainAxisAlignment: MainAxisAlignment.spaceAround,
            // crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Opacity(
              //   opacity: 0,
              //   child: Container(),
              // ),
              const Spacer(flex: 6),
              TextButton(
                onPressed: () {},
                style: ButtonStyle(
                  backgroundColor: MaterialStateProperty.all(Colors.red),
                  // padding: MaterialStateProperty.all<EdgeInsets>(
                  //   const EdgeInsets.only(right: 20, left: 20),
                  // ),
                ),
                child: const Padding(
                  padding: EdgeInsets.symmetric(horizontal: 20.0),
                  child: Text(
                    'End Chat',
                    style: TextStyle(
                      color: Colors.white,
                    ),
                  ),
                ),
              ),
              const Spacer(),
            ],
          ),
        ],
      ),
    );
  }
}
