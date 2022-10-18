import 'package:age_calculator/age_calculator.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:frontend/data/appointments.dart';
import 'package:frontend/models/appointment.dart';
import 'package:frontend/modules/appointment/manage_appointments_screen.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:frontend/services/doctor_service.dart';
import 'package:frontend/services/patient_service.dart';
import 'package:frontend/services/specialty_service.dart';
import 'package:frontend/utility.dart';
import 'package:intl/intl.dart';
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
    when(() => mockAuthService.getUserIdFromStorage())
        .thenAnswer((_) async => id);
  }

  arrangeAuthServiceReturnsRole(String role) {
    when(() => mockAuthService.getUserRoleFromStorage())
        .thenAnswer((_) async => role);
  }

  arrangePatientServiceReturnsAppointments(List<Appointment> res, int id,
      {bool wait = false}) {
    when(() => mockPatientService.fetchPatientAppointments(id))
        .thenAnswer((_) async {
      if (wait) {
        await Future.delayed(const Duration(seconds: 2));
      }
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
        specialtyService: SpecialtyService(),
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

  testWidgets('loading indicator is displayed while fetching appointments',
      (WidgetTester tester) async {
    // fetch should wait 2 seconds and return null
    arrangeAuthServiceReturnsUserId(1);
    arrangeAuthServiceReturnsRole("PATIENT");
    arrangePatientServiceReturnsAppointments([], 1, wait: true);
    await tester.pumpWidget(createWidgetUnderTest());

    // forces a widget rebuild after 500 milliseconds
    await tester.pump(const Duration(milliseconds: 500));

    expect(find.byType(CircularProgressIndicator), findsOneWidget);

    // waits for no more rebuilds happening (the 2s future)
    await tester.pumpAndSettle();
  });

  testWidgets(
    "appointments are displayed for patient",
    (WidgetTester tester) async {
      arrangeAuthServiceReturnsUserId(2);
      arrangeAuthServiceReturnsRole("PATIENT");
      arrangePatientServiceReturnsAppointments(mockAppointments, 2);

      await tester.pumpWidget(createWidgetUnderTest());

      await tester.pump();

      for (final appointment in mockAppointments) {
        expect(
            find.text(
                "${appointment.doctor.firstName} ${appointment.doctor.lastName}"),
            findsOneWidget);
        expect(
            find.text(
                "Age: ${AgeCalculator.age(appointment.doctor.dateOfBirth).years}"),
            findsOneWidget);
        expect(
            find.text(
              DateFormat('dd MMM yyyy').format(appointment.date),
            ),
            findsOneWidget);
        expect(
            find.text(
              '${Utility.timeToString(appointment.startTime)} - ${Utility.timeToString(appointment.endTime)}',
            ),
            findsOneWidget);
      }
    },
  );

  testWidgets(
    "appointments are displayed for doctor",
    (WidgetTester tester) async {
      arrangeAuthServiceReturnsUserId(1);
      arrangeAuthServiceReturnsRole("DOCTOR");
      arrangeDoctorServiceReturnsAppointments(mockAppointments, 1);

      await tester.pumpWidget(createWidgetUnderTest());

      await tester.pump();

      for (final appointment in mockAppointments) {
        expect(
            find.text(
                "${appointment.patient.firstName} ${appointment.patient.lastName}"),
            findsOneWidget);
        expect(
            find.text(
                "Age: ${AgeCalculator.age(appointment.patient.dateOfBirth).years}"),
            findsOneWidget);
        expect(
            find.text(
              DateFormat('dd MMM yyyy').format(appointment.date),
            ),
            findsOneWidget);
        expect(
            find.text(
              '${Utility.timeToString(appointment.startTime)} - ${Utility.timeToString(appointment.endTime)}',
            ),
            findsOneWidget);
      }
    },
  );
}
