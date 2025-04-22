FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests



FROM openjdk:21-jdk-slim

WORKDIR /app

COPY Interns_2025_SWIFT_CODES.csv /app/
COPY ISO2DATA.csv /app/
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
