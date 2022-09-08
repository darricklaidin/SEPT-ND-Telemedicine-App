import 'package:chat_bubbles/chat_bubbles.dart';
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
                fontStyle: FontStyle.italic,
              ),
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
          const SizedBox(
            height: 10,
          ),
          BubbleNormal(
            bubbleRadius: 10,
            delivered: true,
            seen: true,
            text:
                'hello, doctor, i believe i have the \ncoronavirus as i am experiencing \nmild symtoms, what do i do?',
            color: const Color.fromARGB(255, 30, 119, 253),
            tail: true,
            textStyle: const TextStyle(
              color: Colors.white,
              fontSize: 13,
            ),
            sent: true,
          ),
          const SizedBox(
            height: 20,
          ),
          BubbleNormal(
            bubbleRadius: 10,
            text:
                'I\'m here for you, don\'t worry. \nWhat symptoms are you \nexperiencing?',
            color: Colors.blueGrey.shade50,
            tail: true,
            textStyle: const TextStyle(
              color: Colors.black45,
              fontSize: 13,
              fontWeight: FontWeight.bold,
            ),
            isSender: false,
          ),
          const SizedBox(
            height: 20,
          ),
          BubbleNormal(
            bubbleRadius: 10,
            delivered: true,
            seen: true,
            text: 'fever\ndry cough\ntiredness\nsore throat',
            color: const Color.fromARGB(255, 30, 119, 253),
            tail: true,
            textStyle: const TextStyle(
              color: Colors.white,
              fontSize: 13,
            ),
            sent: true,
          ),
          const SizedBox(
            height: 20,
          ),
          BubbleNormal(
            bubbleRadius: 10,
            text:
                'oh so sorry about that. do\nyou have any underlying\ndiseases?',
            color: Colors.blueGrey.shade50,
            tail: true,
            textStyle: const TextStyle(
              color: Colors.black45,
              fontSize: 13,
              fontWeight: FontWeight.bold,
            ),
            isSender: false,
          ),
          const SizedBox(
            height: 20,
          ),
          BubbleNormal(
            bubbleRadius: 10,
            delivered: true,
            seen: true,
            text: 'oh no',
            color: const Color.fromARGB(255, 30, 119, 253),
            tail: true,
            textStyle: const TextStyle(
              color: Colors.white,
              fontSize: 13,
            ),
            sent: true,
          ),
          const Spacer(),
          Container(
            color: Colors.blueGrey.shade50,
            padding: const EdgeInsets.all(8),
            child: Row(
              children: const [
                Icon(
                  Icons.attach_file,
                  size: 35,
                  color: Colors.grey,
                ),
                Expanded(
                  child: Padding(
                    padding: EdgeInsets.symmetric(horizontal: 8.0),
                    child: TextField(
                      decoration: InputDecoration(
                        contentPadding: EdgeInsets.all(10.0),
                        constraints: BoxConstraints(maxHeight: 45),
                        filled: true,
                        fillColor: Colors.white,
                        enabledBorder: OutlineInputBorder(
                          borderSide:
                              BorderSide(color: Colors.transparent, width: 0.0),
                          borderRadius: BorderRadius.all(Radius.circular(10)),
                        ),
                        border: OutlineInputBorder(),
                        hintText: 'Write a message...',
                        hintStyle: TextStyle(
                          color: Colors.black54,
                          fontSize: 15,
                          fontWeight: FontWeight.w300,
                          fontStyle: FontStyle.italic,
                        ),
                      ),
                    ),
                  ),
                ),
                Icon(
                  Icons.send,
                  size: 35,
                  color: Colors.grey,
                )
              ],
            ),
          )
        ],
      ),
    );
  }
}
