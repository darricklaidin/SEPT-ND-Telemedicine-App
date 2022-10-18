import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:frontend/models/appointment.dart';
import 'package:frontend/modules/appointment/manage_appointments_screen.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:frontend/services/doctor_service.dart';
import 'package:frontend/services/patient_service.dart';
import 'package:mocktail/mocktail.dart';

class MockAuthService extends Mock implements AuthService {}

class MockPatientService extends Mock implements PatientService {}

class MockDoctorService extends Mock implements DoctorService {}

class MockNavigatorObserver extends Mock implements NavigatorObserver {}

void main() {
  late AuthService mockAuthService;
  late MockPatientService mockPatientService;
  late MockDoctorService mockDoctorService;
  late NavigatorObserver mockObserver;

  setUp(() {
    mockAuthService = MockAuthService();
    mockPatientService = MockPatientService();
    mockDoctorService = MockDoctorService();
    mockObserver = MockNavigatorObserver();
  });

  arrangeAuthServiceReturnsUserId(int id) {
    when(() => mockAuthService.getUserIdFromStorage()).thenAnswer((_) async {
      return id;
    });
  }

  arrangeAuthServiceReturnsRole(String role) {
    when(() => mockAuthService.getUserRoleFromStorage()).thenAnswer((_) async {
      return role;
    });
  }

  arrangePatientServiceReturnsAppointments(List<Appointment> res, int id) {
    when(() => mockPatientService.fetchPatientAppointments(id))
        .thenAnswer((_) async {
      return res;
    });
  }

  arrangeDoctorServiceReturnsAppointments(List<Appointment> res, int id) {
    when(() => mockDoctorService.fetchDoctorAppointments(id))
        .thenAnswer((_) async {
      return res;
    });
  }

  Widget createWidgetUnderTest() {
    return MaterialApp(
      title: 'SEPT-ND-Telemedicine-App',
      home: ManageAppointmentsScreen(
        handleTabSelection: () => {},
        authService: mockAuthService,
        patientService: mockPatientService,
        doctorService: mockDoctorService,
      ),
      navigatorObservers: [mockObserver],
    );
  }

  testWidgets('title is displayed', (WidgetTester tester) async {
    // check auth should return null to indicate user is not logged in
    arrangeAuthServiceReturnsUserId(1);
    arrangeAuthServiceReturnsRole("PATIENT");
    arrangePatientServiceReturnsAppointments([], 1);
    await tester.pumpWidget(createWidgetUnderTest());
    expect(find.text('Appointments'), findsOneWidget);
  });
}
