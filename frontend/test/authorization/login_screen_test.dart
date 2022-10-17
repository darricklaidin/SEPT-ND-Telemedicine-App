import 'package:flutter_test/flutter_test.dart';
import 'package:frontend/modules/authorization/login_screen.dart';

void main() {
  testWidgets('Test app', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(const LoginScreen());
  });
}
