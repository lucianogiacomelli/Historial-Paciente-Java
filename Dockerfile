# Etapa 1: Build
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./
RUN chmod +x gradlew

COPY src ./src
RUN ./gradlew bootJar --no-daemon -x test

# Etapa 2: Runtime (solo lo necesario)
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
