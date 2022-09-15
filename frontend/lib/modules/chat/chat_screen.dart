import 'package:flutter/material.dart';
import 'package:chat_bubbles/chat_bubbles.dart';

import '../../data/messages_data.dart';

class ChatScreen extends StatelessWidget {
  ChatScreen({Key? key}) : super(key: key);

  static const routeName = '/chat';
  final ScrollController _scrollController = ScrollController();

  @override
  Widget build(BuildContext context) {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _scrollController.jumpTo(_scrollController.position.maxScrollExtent);
    });

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
          _buildChatScrollView(),
          const SizedBox(
            height: 5,
          ),
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

  // Widget _buildChatScrollView() {
  //   return Expanded(
  //       child: ListView(
  //     scrollDirection: Axis.vertical,
  //     shrinkWrap: true,
  //     reverse: true,
  //     children: _buildMessages(),
  //   ));
  // }

  Expanded _buildChatScrollView() {
    return Expanded(
        child: ListView.builder(
            scrollDirection: Axis.vertical,
            controller: _scrollController,
            shrinkWrap: true,
            itemCount: messageList.length,
            // prototypeItem: ListTile(
            //   title: Text(items.first),
            // ),
            itemBuilder: (context, index) {
              return Padding(
                padding: const EdgeInsets.only(top: 20),
                child: BubbleNormal(
                  bubbleRadius: 10,
                  delivered: true,
                  seen: true,
                  text: messageList[index].text,
                  color: messageList[index].isSender
                      ? const Color.fromARGB(255, 30, 119, 253)
                      : Colors.blueGrey.shade50,
                  tail: true,
                  textStyle: TextStyle(
                      color: messageList[index].isSender
                          ? Colors.white
                          : Colors.black45,
                      fontSize: 13,
                      fontWeight: messageList[index].isSender
                          ? FontWeight.normal
                          : FontWeight.bold),
                  sent: true,
                  isSender: messageList[index].isSender,
                ),
              );
            }));
  }

  // return messageList.map(
  //   (msg) {
  //     return Padding(
  //       padding: const EdgeInsets.only(top: 20),
  //       child: BubbleNormal(
  //         bubbleRadius: 10,
  //         delivered: true,
  //         seen: true,
  //         text: msg.text,
  //         color: msg.isSender
  //             ? const Color.fromARGB(255, 30, 119, 253)
  //             : Colors.blueGrey.shade50,
  //         tail: true,
  //         textStyle: TextStyle(
  //             color: msg.isSender ? Colors.white : Colors.black45,
  //             fontSize: 13,
  //             fontWeight: msg.isSender ? FontWeight.normal : FontWeight.bold),
  //         sent: true,
  //         isSender: msg.isSender,
  //       ),
  //     );
  //   },
  // ).toList();
}
