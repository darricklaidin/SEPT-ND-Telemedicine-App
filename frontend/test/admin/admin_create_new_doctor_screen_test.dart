import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:frontend/models/api_response.dart';
import 'package:frontend/models/specialty.dart';
import 'package:frontend/modules/admin/admin_create_new_doctor_screen.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:frontend/services/specialty_service.dart';
import 'package:mocktail/mocktail.dart';

class MockAuthService extends Mock implements AuthService {}

class MockSpecialtyService extends Mock implements SpecialtyService {}

class MockNavigatorObserver extends Mock implements NavigatorObserver {}

void main() {
  late AuthService mockAuthService;
  late MockSpecialtyService mockSpecialtyService;
  late NavigatorObserver mockObserver;
  Specialty mockSpecialty = Specialty(specialtyID: 1, specialtyName: "GP");

  setUp(() {
    mockAuthService = MockAuthService();
    mockSpecialtyService = MockSpecialtyService();
    mockObserver = MockNavigatorObserver();
  });

  arrangeSpecialtyServiceReturnsSpecialties() {
    when(() => mockSpecialtyService.getSpecialties())
        .thenAnswer((_) async => [mockSpecialty]);
  }

  arrangeAuthServiceRegisterDoctor(String firstName, String lastName,
      String email, String password, String dateOfBirth, int specialtyID) {
    when(() => mockAuthService.registerDoctor(
            firstName, lastName, email, password, dateOfBirth, specialtyID))
        .thenAnswer((_) async {
      ApiResponse res = ApiResponse();
      res.success = true;
      return res;
    });
  }

  Widget createWidgetUnderTest() {
    return MaterialApp(
      title: 'SEPT-ND-Telemedicine-App',
      home: AdminCreateNewDoctorScreen(
        authService: mockAuthService,
        specialtyService: SpecialtyService(),
      ),
      navigatorObservers: [mockObserver],
    );
  }

  testWidgets('title is displayed', (WidgetTester tester) async {
    // check auth should return null to indicate user is not logged in
    await tester.pumpWidget(createWidgetUnderTest());
    expect(find.text('Register New Doctor'), findsOneWidget);
  });
}
