const String nimeshNetwork = '10.0.0.82';
const String androidLocalHost = '10.0.2.2';

const String authPort = '8080';
const String bookingApiPort = '8081';

// Change the local host depending on which device/emulator you are using
const String localhost = androidLocalHost;
const String apiAuthRootUrl = 'http://$localhost:$authPort/api';
const String apiBookingRootUrl = 'http://$localhost:$bookingApiPort/api';

// Persistent Bottom Navigation Bar Page Indexes
const int homePageIndex = 0;
const int searchPageIndex = 1;
const int appointmentsPageIndex = 2;

// Limit bound for dates
DateTime minDate = DateTime(0000, 1, 1);
DateTime maxDate = DateTime(9999, 12, 31);
