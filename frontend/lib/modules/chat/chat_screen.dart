import 'package:flutter/material.dart';
import 'package:persistent_bottom_nav_bar/persistent-tab-view.dart';
import 'package:talkjs_flutter/talkjs_flutter.dart';

import '../home/home_screen.dart';
import '../../service/user_service.dart';

class ChatScreen extends StatefulWidget {
  const ChatScreen({Key? key}) : super(key: key);

  @override
  State<ChatScreen> createState() => _ChatScreenState();
}

class _ChatScreenState extends State<ChatScreen> {
  late Session _session;
  late User _me;
  late User _other;
  late Conversation _conversation;
  bool isPatient = false;

  Future? _myFuture;

  @override
  void initState() {
    super.initState();
    _myFuture = _createChatRoom();
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
        future: _myFuture,
        builder: (context, snapshot) {
          Widget body;

          if (snapshot.connectionState == ConnectionState.waiting) {
            body = const Center(child: CircularProgressIndicator());
          } else {
            if (snapshot.hasError) {
              body = Center(child: Text('Error: ${snapshot.error}'));
            } else {
              body = ChatBox(
                session: _session,
                conversation: _conversation,
              );
            } // snapshot.data  :- get your object which is pass from your downloadData() function
          }

          return Scaffold(
            appBar: AppBar(
                backgroundColor: Colors.transparent,
                elevation: 0,
                title: _buildTitleBar(),
                leading: IconButton(
                  icon: const Icon(Icons.arrow_back,
                      size: 20, color: Colors.black),
                  onPressed: () => pushNewScreen(
                    context,
                    screen: const HomeScreen(),
                  ),
                )),
            body: body,
          );
        });
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

  Future<void> _createChatRoom() async {
    // create a TalkJS session
    _session = Session(appId: 'taq5zIcQ');

    // create a TalkJS user
    final me = isPatient ? await getPatient() : await getDoctor();
    _me = _session.getUser(
      id: '${(isPatient ? 'p' : 'd')}${me.id.toString()}', // create unique depending on user type
      name: '${me.firstName} ${me.lastName}',
      email: [me.email],
      photoUrl: 'https://talkjs.com/images/avatar-1.jpg',
    );

    // set the active TalkJS user to the session
    _session.me = _me;

    // create another user to create a conversation with
    final other = isPatient ? await getDoctor() : await getPatient();

    _other = _session.getUser(
      id: '${(isPatient ? 'p' : 'd')}${other.id.toString()}',
      name: '${other.firstName} ${other.lastName}',
      email: [other.email],
      photoUrl: 'https://talkjs.com/images/avatar-1.jpg',
    );

    // create chat
    _conversation = _session.getConversation(
      id: Talk.oneOnOneId(_me.id, _other.id),
      participants: {Participant(_me), Participant(_other)},
    );
  }
}
