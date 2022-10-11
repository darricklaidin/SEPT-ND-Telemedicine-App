FROM maven:3.6.3-jdk-11
COPY . .
RUN mvn -f pom.xml dependency:go-offline
CMD mvn spring-boot:run
