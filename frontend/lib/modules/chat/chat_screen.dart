import 'package:flutter/material.dart';

/// Displays chat UI
class ChatScreen extends StatelessWidget {
  const ChatScreen({Key? key}) : super(key: key);

  static const routeName = '/chat';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        title: Row(
          children: const [
            CircleAvatar(
              backgroundImage: NetworkImage(
                  'https://media.istockphoto.com/photos/portrait-of-a-doctor-picture-id92347250?k=20&m=92347250&s=612x612&w=0&h=RsbWyj485Bf1T6p2GvaZBQhRhGufqKWdRVdO0sdCcSA='),
            ),
            SizedBox(
              width: 10,
            ),
            Text(
              'Dr. Bellamy Nicholas',
              style: TextStyle(
                  color: Colors.black54,
                  fontSize: 15,
                  fontWeight: FontWeight.w300,
                  fontStyle: FontStyle.italic),
            ),
          ],
        ),
        leading: const Icon(
          Icons.arrow_back,
          color: Colors.black,
          size: 20,
        ),
      ),
      body: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: [
              SizedBox(
                height: 29,
                child: TextButton(
                  onPressed: () {},
                  style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(Colors.red),
                    padding: MaterialStateProperty.all(
                      const EdgeInsets.symmetric(horizontal: 25, vertical: 0),
                    ),
                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                      RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8.0),
                      ),
                    ),
                  ),
                  child: const Text(
                    'End Chat',
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 11,
                    ),
                  ),
                ),
              ),
              const SizedBox(
                width: 30,
              )
            ],
          ),
          const Divider(
            thickness: 1,
            height: 24,
            color: Colors.black38,
          ),
          const Spacer(),
          Row(
            children: const [
              Icon(Icons.attach_file),
              Spacer(),
              Icon(Icons.send)
            ],
          )
        ],
      ),
    );
  }
}
