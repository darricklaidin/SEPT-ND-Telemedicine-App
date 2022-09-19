const String nimeshNetwork = '10.0.0.82';
const String androidLocalHost = '10.0.2.2';

const String authPort = '8080';
const String bookingApiPort = '8081';

// Change the local host depending on which device/emulator you are using
const String localhost = nimeshNetwork;
const String apiAuthRootUrl = 'http://$localhost:$authPort/api';
const String apiBookingRootUrl = 'http://$localhost:$bookingApiPort/api';
