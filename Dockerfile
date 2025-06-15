#FROM eclipse-temurin:17-jdk
#VOLUME /tmp
#ARG JAR_FILE=target/dailydone-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

# Multi-stage build für optimierte Production-Images

# Build Stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Maven wrapper und pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:resolve

# Copy source code
COPY src ./src

# Build application
RUN ./mvnw clean package -DskipTests

# Production Stage
FROM eclipse-temurin:21-jre-jammy AS production

# Create non-root user
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/daily-done-1.0.0.jar app.jar

# Change ownership to non-root user
RUN chown appuser:appuser app.jar

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
