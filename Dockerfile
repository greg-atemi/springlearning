# Use an OpenJDK 17 image as a base
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project file (pom.xml) first to leverage caching
COPY pom.xml .

# Download and cache dependencies (skip tests)
RUN mvn dependency:go-offline -DskipTests -B

# Copy the application source code
COPY src ./src

# Build the application, skip both unit and integration tests
RUN mvn package -DskipTests -Dmaven.test.skip=true

# Use a lightweight base image for the runtime environment
FROM eclipse-temurin:21-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built .jar file from the builder stage
COPY --from=builder /app/target/*.jar app/demo.jar

# Expose the port your application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app/demo.jar"]
