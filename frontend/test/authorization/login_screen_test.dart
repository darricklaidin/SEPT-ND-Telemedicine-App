import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:frontend/modules/authorization/login_screen.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:mocktail/mocktail.dart';

class MockAuthService extends Mock implements AuthService {}

void main() {
  AuthService mockAuthService = MockAuthService();

  Widget createWidgetUnderTest() {
    return MaterialApp(
      title: 'SEPT-ND-Telemedicine-App',
      home: LoginScreen(authService: mockAuthService),
    );
  }

  arrangeAuthServiceReturnsNullAuth(bool wait) {
    when(() => mockAuthService.checkAuth()).thenAnswer((_) async {
      if (wait) {
        await Future.delayed(const Duration(seconds: 2));
      }
      return null;
    });
  }

  testWidgets('title is displayed', (WidgetTester tester) async {
    // check auth should return null to indicate user is not logged in
    arrangeAuthServiceReturnsNullAuth(false);
    await tester.pumpWidget(createWidgetUnderTest());
    expect(find.text('Login'), findsOneWidget);
  });

  testWidgets('loading indicator is displayed while checking auth',
      (WidgetTester tester) async {
    // check auth should wait 2 seconds and return null
    arrangeAuthServiceReturnsNullAuth(true);
    await tester.pumpWidget(createWidgetUnderTest());

    // forces a widget rebuild after 500 milliseconds
    await tester.pump(const Duration(milliseconds: 500));

    expect(find.byType(CircularProgressIndicator), findsOneWidget);

    // waits for no more rebuilds happening (the 2s future)
    await tester.pumpAndSettle();
  });
}
