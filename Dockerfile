# syntax=docker/dockerfile:1

# ─────────────────────────────────────────────────────────────────────────────
# Build stage — compiles the Spring Boot app AND the webpack frontend.
# The frontend-maven-plugin (bound to the Maven build) runs `npm install` and
# `npm run build`, so package.json / lockfile / webpack / tsconfig MUST be
# present before `mvn package` or the frontend build fails.
# ─────────────────────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-25-alpine AS build
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download Maven dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Frontend build inputs — required by frontend-maven-plugin during `mvn package`
COPY package.json package-lock.json ./
COPY tsconfig.json webpack.config.js ./

# Maven wrapper + source
COPY mvnw .
COPY .mvn .mvn
COPY src src

# Build the application (frontend is compiled and bundled into the jar)
RUN mvn package -DskipTests -B

# ─────────────────────────────────────────────────────────────────────────────
# Runtime stage
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy the built jar
COPY --from=build /app/target/*.jar app.jar
RUN chown -R appuser:appgroup /app
USER appuser

# Default runtime profile (override in Portainer stack / docker-compose env)
ENV SPRING_PROFILES_ACTIVE=docker
ENV SERVER_PORT=8085

# App listens on 8085 (matches application.yml server.port)
EXPOSE 8085

# Health check hits the actuator health endpoint on the same port
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget -qO- "http://localhost:${SERVER_PORT}/actuator/health" || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
