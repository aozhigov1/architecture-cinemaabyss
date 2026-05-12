# --- Stage 1: build ---
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Сначала только wrapper и build-файлы — слой с загрузкой зависимостей
# будет переиспользоваться, пока build.gradle/settings.gradle не меняются.
COPY gradlew ./
COPY gradle gradle
COPY settings.gradle build.gradle ./

RUN chmod +x ./gradlew && ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true

# Теперь исходники и собственно сборка fat-JAR.
COPY src src
RUN ./gradlew --no-daemon bootJar -x test

# --- Stage 2: runtime ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/proxy-service-1.0.0.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
