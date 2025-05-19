FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/auth-service-0.0.1-SNAPSHOT.jar /app/auth-service.jar

CMD ["java", "-jar", "auth-service.jar"]
