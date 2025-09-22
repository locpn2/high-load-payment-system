# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and the project description file
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download project dependencies
RUN ./mvnw dependency:go-offline

# Copy the project source
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Expose port 8082
EXPOSE 8082

# Chạy ứng dụng
CMD ["java", "-jar", "target/inquiry-service-0.0.1-SNAPSHOT.jar"]
