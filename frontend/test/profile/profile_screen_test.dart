import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:frontend/models/patient.dart';
import 'package:frontend/models/user.dart';
import 'package:frontend/modules/profile/profile_screen.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:frontend/services/doctor_service.dart';
import 'package:frontend/services/specialty_service.dart';
import 'package:mocktail/mocktail.dart';

class MockAuthService extends Mock implements AuthService {}

class MockDoctorService extends Mock implements DoctorService {}

class MockSpecialtyService extends Mock implements SpecialtyService {}

class MockNavigatorObserver extends Mock implements NavigatorObserver {}

void main() {
  late AuthService mockAuthService;
  late MockDoctorService mockDoctorService;
  late MockSpecialtyService mockSpecialtyService;
  late NavigatorObserver mockObserver;

  setUp(() {
    mockAuthService = MockAuthService();
    mockDoctorService = MockDoctorService();
    mockSpecialtyService = MockSpecialtyService();
    mockObserver = MockNavigatorObserver();
  });

  Widget createWidgetUnderTest(
    User user,
    String userRole,
  ) {
    return MaterialApp(
      title: 'SEPT-ND-Telemedicine-App',
      home: ProfileScreen(
        user: user,
        userRole: userRole,
        authService: mockAuthService,
        doctorService: mockDoctorService,
        specialtyService: mockSpecialtyService,
      ),
      navigatorObservers: [mockObserver],
    );
  }

  testWidgets('user profile is displayed', (WidgetTester tester) async {
    User mockUser = Patient(
      userID: 1,
      firstName: 'Darrick',
      lastName: "Hong",
      dateOfBirth: DateTime.now(),
      email: 'email@email.com',
      accountStatus: true,
      symptoms: "symptoms",
    );
    await tester.pumpWidget(createWidgetUnderTest(mockUser, 'DOCTOR'));
    expect(find.text('${mockUser.firstName} ${mockUser.lastName}'),
        findsOneWidget);
  });
}
