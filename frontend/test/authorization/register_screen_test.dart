import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:frontend/modules/authorization/register_screen.dart';
import 'package:frontend/modules/home/main_screen.dart';
import 'package:frontend/services/auth_service.dart';
import 'package:mocktail/mocktail.dart';

class MockAuthService extends Mock implements AuthService {}

class MockNavigatorObserver extends Mock implements NavigatorObserver {}

void main() {
  late AuthService mockAuthService;
  late NavigatorObserver mockObserver;

  setUp(() {
    mockAuthService = MockAuthService();
    mockObserver = MockNavigatorObserver();
  });

  Widget createWidgetUnderTest() {
    return MaterialApp(
      title: 'SEPT-ND-Telemedicine-App',
      home: RegisterScreen(authService: mockAuthService),
      // This mocked observer will now receive all navigation events
      // that happen in our app.
      navigatorObservers: [mockObserver],
      routes: {
        "/home": (context) => const MainScreen(),
      },
    );
  }

  testWidgets('title is displayed', (WidgetTester tester) async {
    // check auth should return null to indicate user is not logged in
    await tester.pumpWidget(createWidgetUnderTest());
    expect(find.text('Register'), findsOneWidget);
  });

  testWidgets('empty email and password validaton',
      (WidgetTester tester) async {
    // load screen
    await tester.pumpWidget(createWidgetUnderTest());
    await tester.pump();

    // test register button click with empty input
    Finder btn = find.byKey(const Key('register'));
    await tester.tap(btn);
    await tester.pumpAndSettle();

    // expect validator error messages
    expect(find.text('Invalid Date of Birth'), findsOneWidget);
  });
}
