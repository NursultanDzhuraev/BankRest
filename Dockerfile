#  Build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
RUN ./gradlew bootJar --no-daemon -x test
# Rumtime
FROM eclipse-temurin:21-jre-alpine AS rumtime
RUN addgroup -S addgroup && adduser -S adduser -G addgroup
USER appuser
WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar app.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 Djava.security.egd=file:/dev/./urandom"
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
 CMD wget -qO- http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["sh", "-c", "java$JAVA_OPTS -jat app.jar"]
