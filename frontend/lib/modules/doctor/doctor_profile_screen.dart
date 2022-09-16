import 'package:flutter/material.dart';

class DoctorProfileScreen extends StatelessWidget {
  const DoctorProfileScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.transparent,
        elevation: 0,
        title: _buildTitleRow(),
        leading: const Icon(
          Icons.arrow_back,
          color: Colors.black,
          size: 20,
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 40.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            ..._buildProfileHeader(),
            _buildExperienceCard(),
            ..._buildAboutSection(),
            ..._buildBookAppointmentBtn()
          ],
        ),
      ),
    );
  }

  Row _buildTitleRow() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.end,
      children: const [
        CircleAvatar(
          backgroundImage: NetworkImage(
              'https://media.istockphoto.com/photos/portrait-of-a-doctor-picture-id92347250?k=20&m=92347250&s=612x612&w=0&h=RsbWyj485Bf1T6p2GvaZBQhRhGufqKWdRVdO0sdCcSA='),
        ),
        SizedBox(
          width: 20,
        )
      ],
    );
  }

  Card _buildExperienceCard() {
    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 110),
      elevation: 0,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(15.0),
      ),
      child: Column(
        children: const [
          SizedBox(
            height: 20,
          ),
          Icon(
            Icons.verified_outlined,
            color: Colors.red,
          ),
          SizedBox(
            height: 10,
          ),
          Text('10 Yrs'),
          SizedBox(
            height: 5,
          ),
          Text(
            'Experience',
            style: TextStyle(color: Colors.grey, fontSize: 10),
          ),
        ],
      ),
    );
  }

  List<Widget> _buildProfileHeader() {
    return [
      const Spacer(),
      const Center(
        child: CircleAvatar(
          radius: 55,
          backgroundImage: NetworkImage(
              'https://media.istockphoto.com/photos/portrait-of-a-doctor-picture-id92347250?k=20&m=92347250&s=612x612&w=0&h=RsbWyj485Bf1T6p2GvaZBQhRhGufqKWdRVdO0sdCcSA='),
        ),
      ),
      const SizedBox(
        height: 20,
      ),
      const Text(
        'Dr. Dylan McBlue',
        style: TextStyle(fontSize: 21, wordSpacing: 1),
        textAlign: TextAlign.center,
      ),
      const Text(
        'General Physician',
        style: TextStyle(fontSize: 18, color: Colors.grey),
        textAlign: TextAlign.center,
      ),
      const SizedBox(
        height: 10,
      )
    ];
  }

  List<Widget> _buildAboutSection() {
    return [
      const Spacer(),
      const Text(
        'About Doctor',
        style: TextStyle(
            fontSize: 17, color: Colors.grey, fontStyle: FontStyle.italic),
      ),
      const SizedBox(
        height: 10,
      ),
      const Text(
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Odio ut sem nulla pharetra diam sit amet nisl. Lacus laoreet non curabitur gravida arcu ac. Nulla pellentesque dignissim enim sit amet venenatis urna cursus eget. ',
        style: TextStyle(
          fontSize: 12,
        ),
        textAlign: TextAlign.justify,
      ),
      const Spacer(
        flex: 2,
      )
    ];
  }

  List<Widget> _buildBookAppointmentBtn() {
    return [
      Center(
        child: SizedBox(
          height: 40,
          child: TextButton(
            onPressed: () {},
            style: ButtonStyle(
              backgroundColor: MaterialStateProperty.all(
                  const Color.fromARGB(255, 237, 201, 94)),
              padding: MaterialStateProperty.all(
                const EdgeInsets.symmetric(horizontal: 65, vertical: 0),
              ),
              shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8.0),
                ),
              ),
            ),
            child: const Text(
              'Book Appointment',
              style: TextStyle(
                color: Colors.white,
                fontSize: 14,
              ),
            ),
          ),
        ),
      ),
      const Spacer(
        flex: 3,
      ),
    ];
  }
}
