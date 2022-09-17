import 'package:flutter/material.dart';
import 'package:persistent_bottom_nav_bar/persistent-tab-view.dart';
import 'package:talkjs_flutter/talkjs_flutter.dart';

import '../home/home_screen.dart';

class ChatScreen extends StatelessWidget {
  ChatScreen({Key? key}) : super(key: key);

  final ScrollController _scrollController = ScrollController();

  @override
  Widget build(BuildContext context) {
    // create a TalkJS session
    final session = Session(appId: 'taq5zIcQ');

    // create a TalkJS user
    final me = session.getUser(
      id: '123456',
      name: 'Alice',
      email: ['alice@example.com'],
      photoUrl: 'https://talkjs.com/images/avatar-1.jpg',
      welcomeMessage: 'Hey there! How are you? :-)',
      role: 'default',
    );

    // set the active TalkJS user to the session
    session.me = me;

    // create another user to create a conversation with
    final other = session.getUser(
      id: '654321',
      name: 'Sebastian',
      email: ['Sebastian@example.com'],
      photoUrl: 'https://talkjs.com/images/avatar-5.jpg',
      welcomeMessage: 'Hey, how can I help?',
      role: 'default',
    );

    // create chat
    final conversation = session.getConversation(
      id: Talk.oneOnOneId(me.id, other.id),
      participants: {Participant(me), Participant(other)},
    );

    // scroll to latest message on build
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _scrollController.jumpTo(_scrollController.position.maxScrollExtent);
    });

    return Scaffold(
      appBar: AppBar(
          backgroundColor: Colors.transparent,
          elevation: 0,
          title: _buildTitleBar(),
          leading: IconButton(
            icon: const Icon(Icons.arrow_back, size: 20, color: Colors.black),
            onPressed: () => pushNewScreen(
              context,
              screen: const HomeScreen(),
            ),
          )),
      body: ChatBox(
        session: session,
        conversation: conversation,
      ),
    );
  }

  Row _buildTitleBar() {
    return Row(
      children: const [
        SizedBox(
          width: 10,
        ),
        Text(
          'Appointment',
          style: TextStyle(
            color: Colors.black54,
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
      ],
    );
  }
}
