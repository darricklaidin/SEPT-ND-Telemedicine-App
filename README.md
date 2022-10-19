# ND Telemedicine App
## Software Engineering Process and Tools Assignment Project

This is a web/mobile application that can be used by anyone, allowing patients to signup add their health information and make a profile, book an appointment with GP through calendar, text chat with Doctor, and view their prescribed medicine. User also can add their status such fever, pain and so on. Doctors(or GP) can add their availabilities, chat with their client, prescribe and manage medicine, and view their patients’ health status . The app can have super admin user to manage both type of users. The application also can notify users for their appointment or medicine time.

### Members:
- Darrick Edbert Laidin (s3905278): Scrum Master
- W Nimesh C De Silva (s3831242): Tech Lead
- Bryan Hong (s3679989)
- Mahamed Ali Mohamed (s3839971)
- Hirday Bajaj (s3901303)

### Product Owner:
Amir Homayoon Ashrafzadeh

## Build & Run via GitHub Repository

### Configuration
1. Download the `.zip` file on the `main` branch from the GitHub Repository.
2. You can choose to either run the server locally or through docker.
	- Using Docker:
		1. Install **Docker**
	- Using local machine:
		1. Install **Maven** and **Java (JDK 11)** on your local machine
		2. Install **MySQL** on your local machine and ensure that it is running in the background
		Install **Flutter** on your local machine and either have a **mobile emulator** open or run the application front-end on a **physically connected mobile device**. 
			Note: If you choose to run the front-end on a physically connected mobile device, you will have to change the `localhost` variable in the `/frontend/lib/config/constants.dart`. This value will vary based on your network IP.

### Steps to build & run on local machine
To run the application on your local machine, follow these steps:
1. For each microservice:
	1. Navigate to their root directory (e.g. `/backend/appointment-microservice/`, `/backend/auth-microservice/`, etc.). This should be where you can find their `pom.xml` files.
	2. When on this path, run the command `mvn spring-boot:run`.
2. Run `main.dart` in the `/frontend/lib/` directory.

### Steps to build & run the docker container
To run the server in a docker container, follow these steps:
1. Open the terminal in the `root` directory of the repository (where the `docker-compose.yml` file is located).
2. Before you run any commands, check that this container does not already exist. If there is already this existing container, remove that container.
3. Run the command `docker-compose up`. This will take several minutes.
4. Once the container is running, the server should be accessible on your local machine through the Docker container.
5. Then, run `main.dart` in the `/frontend/lib/` directory.

#### Port mappings
For reference, these are the port mappings for the docker container:
- MySQL Ports: 
	- Local: 3307
	- Docker: 3306
- Spring Boot (Auth Microservice)
	- Local: 9090
	- Docker: 8080
- Spring Boot (Appointment Microservice)
	- Local: 9091
	- Docker: 8081
- Spring Boot (Prescription Microservice)
	- Local: 9092
	- Docker: 8082
