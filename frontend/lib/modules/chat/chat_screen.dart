import 'package:flutter/material.dart';
import 'package:talkjs_flutter/talkjs_flutter.dart';

import '../../services/doctor_service.dart';
import '../../services/patient_service.dart';

class ChatScreen extends StatefulWidget {
  final Function handleTabSelection;
  final int patientID;
  final int doctorID;
  final bool isPatient;

  const ChatScreen({
    Key? key,
    required this.patientID,
    required this.doctorID,
    required this.isPatient,
    required this.handleTabSelection,
  }) : super(key: key);

  @override
  State<ChatScreen> createState() => _ChatScreenState();
}

class _ChatScreenState extends State<ChatScreen> {
  late Session _session;
  late User _me;
  late User _other;
  late Conversation _conversation;
  String patientImgUrl =
      'https://e7.pngegg.com/pngimages/369/691/png-clipart-avatar-computer-icons-patient-avatar-heroes-logo.png';
  String doctorImgUrl =
      'https://toppng.com/uploads/preview/logo-doctors-logo-black-and-white-vector-11563999612kv1q84czrt.png';
  Future? _myFuture;

  @override
  void initState() {
    super.initState();
    _myFuture =
        _createChatRoom(widget.patientID, widget.doctorID, widget.isPatient);
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
                  onPressed: () => {Navigator.pop(context)},
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
          'Appointment Chat',
          style: TextStyle(
            color: Colors.black54,
            fontSize: 18,
            fontWeight: FontWeight.bold,
          ),
        ),
      ],
    );
  }

  Future<void> _createChatRoom(
      int patientID, int doctorID, bool isPatient) async {
    // create a TalkJS session
    _session = Session(appId: 'taq5zIcQ');

    // create a TalkJS user
    final me = isPatient
        ? await PatientService.fetchPatient(patientID)
        : await DoctorService.fetchDoctor(doctorID);
    _me = _session.getUser(
      id: '${(isPatient ? 'p' : 'd')}${me.userID.toString()}', // create unique depending on user type
      name: '${me.firstName} ${me.lastName}',
      email: [me.email],
      photoUrl: isPatient ? patientImgUrl : doctorImgUrl,
    );

    // set the active TalkJS user to the session
    _session.me = _me;

    // create another user to create a conversation with
    final other = isPatient
        ? await DoctorService.fetchDoctor(doctorID)
        : await PatientService.fetchPatient(patientID);

    _other = _session.getUser(
      id: '${(isPatient ? 'p' : 'd')}${other.userID.toString()}',
      name: '${other.firstName} ${other.lastName}',
      email: [other.email],
      photoUrl: isPatient ? doctorImgUrl : patientImgUrl,
    );

    // create chat
    _conversation = _session.getConversation(
      id: Talk.oneOnOneId(_me.id, _other.id),
      participants: {Participant(_me), Participant(_other)},
    );
  }
}
