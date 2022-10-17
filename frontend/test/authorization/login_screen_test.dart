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

  testWidgets('empty email and password validaton',
      (WidgetTester tester) async {
    // load screen
    arrangeAuthServiceReturnsNullAuth(false);
    await tester.pumpWidget(createWidgetUnderTest());
    await tester.pump();

    // test login button click with empty input
    await tester.tap(find.byType(ElevatedButton));
    await tester.pumpAndSettle();

    // expect validator error messages
    expect(find.text('Password cannot be empty'), findsOneWidget);
    expect(find.text('Email cannot be empty'), findsOneWidget);
  });

  testWidgets(
      'non-empty email and password, valid account, call sign in, succeed',
      (WidgetTester tester) async {
    // load screen
    arrangeAuthServiceReturnsNullAuth(false);
    await tester.pumpWidget(createWidgetUnderTest());
    await tester.pump();

    // test login button click with non-empty input
    Finder email = find.byKey(const Key('email'));
    Finder pwd = find.byKey(const Key('password'));

    await tester.enterText(email, "email@email.com");
    await tester.enterText(pwd, "123456textemptynot");
    await tester.pump();

    // expect validator error messages
    expect(find.text('Password cannot be empty'), findsOneWidget);
    expect(find.text('Email cannot be empty'), findsOneWidget);
  });
}
