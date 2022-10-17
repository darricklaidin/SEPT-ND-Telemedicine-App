import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:frontend/models/api_response.dart';
import 'package:frontend/modules/authorization/login_screen.dart';
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
      home: LoginScreen(authService: mockAuthService),
      // This mocked observer will now receive all navigation events
      // that happen in our app.
      navigatorObservers: [mockObserver],
      routes: {
        "/home": (context) => const MainScreen(),
      },
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

  arrangeAuthServiceReturnsLoginSuccess(
      String email, String password, bool res) {
    when(() => mockAuthService.loginUser(email, password))
        .thenAnswer((_) async {
      ApiResponse apiRespone = ApiResponse();
      apiRespone.success = res;
      return apiRespone;
    });
  }

  arrangeAuthServiceReturnsRole(String role) {
    when(() => mockAuthService.getUserRoleFromStorage()).thenAnswer((_) async {
      return role;
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
    expect(find.text('Email cannot be empty'), findsOneWidget);
    expect(find.text('Password cannot be empty'), findsOneWidget);
  });

  testWidgets('invalid email and password validaton',
      (WidgetTester tester) async {
    // load screen
    arrangeAuthServiceReturnsNullAuth(false);
    await tester.pumpWidget(createWidgetUnderTest());
    await tester.pump();

    // test login button click with invalid input
    String invalidEmail = "invalidemail.com"; // invalid email syntax
    String invalidPassword = "123"; // too short
    Finder email = find.byKey(const Key('email'));
    Finder pwd = find.byKey(const Key('password'));

    await tester.enterText(email, invalidEmail);
    await tester.enterText(pwd, invalidPassword);
    await tester.tap(find.byType(ElevatedButton));
    await tester.pumpAndSettle();

    // expect validator error messages
    expect(find.text('Invalid email'), findsOneWidget);
    expect(
        find.text('Password should be atleast 8 characters'), findsOneWidget);
  });

  testWidgets(
      'valid email and password, valid account, call loginUser, succeed',
      (WidgetTester tester) async {
    String validEmail = "email@email.com";
    String validPassword = "123456textemptynot";

    // load screen with required auth returns
    arrangeAuthServiceReturnsNullAuth(false);
    arrangeAuthServiceReturnsRole('USER');
    arrangeAuthServiceReturnsLoginSuccess(validEmail, validPassword, true);
    await tester.pumpWidget(createWidgetUnderTest());
    await tester.pump();

    // test login button click with non-empty input
    Finder email = find.byKey(const Key('email'));
    Finder pwd = find.byKey(const Key('password'));

    await tester.enterText(email, validEmail);
    await tester.enterText(pwd, validPassword);
    await tester.tap(find.byType(ElevatedButton));
    await tester.pump();

    // expect loginUser being called and success snackbar shown
    verify(() => mockAuthService.loginUser(validEmail, validPassword))
        .called(1);
    expect(find.text('Login Successful'), findsOneWidget);

    /// Verify that a push event happened
    verify(
      () => mockObserver.didReplace(
        oldRoute: any(named: 'oldRoute'),
        newRoute: any(named: 'newRoute'),
      ),
    );
  });

  testWidgets('valid email and password, valid account, call loginUser, fail',
      (WidgetTester tester) async {
    String validEmail = "email@email.com";
    String validPassword = "123456textemptynot";

    // load screen with required auth returns
    arrangeAuthServiceReturnsNullAuth(false);
    arrangeAuthServiceReturnsLoginSuccess(validEmail, validPassword, false);
    await tester.pumpWidget(createWidgetUnderTest());
    await tester.pump();

    // test login button click with non-empty input
    Finder email = find.byKey(const Key('email'));
    Finder pwd = find.byKey(const Key('password'));

    await tester.enterText(email, validEmail);
    await tester.enterText(pwd, validPassword);
    await tester.tap(find.byType(ElevatedButton));
    await tester.pump();

    // expect loginUser being called and failure snackbar shown
    verify(() => mockAuthService.loginUser(validEmail, validPassword))
        .called(1);
    expect(find.text('Invalid Credentials'), findsOneWidget);
  });
}
