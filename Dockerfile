FROM openjdk:17-alpine
WORKDIR /app
COPY target/football-manager-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
