FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY gradlew ./
COPY gradle gradle
COPY settings.gradle build.gradle ./

RUN chmod +x ./gradlew && ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true

COPY src src
RUN ./gradlew --no-daemon bootJar -x test

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/proxy-service-1.0.0.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
