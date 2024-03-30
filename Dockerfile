# Use the official Maven image to create a build artifact
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven pom.xml file
COPY pom.xml .

# Copy the source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use the official OpenJDK image for the runtime
FROM openjdk:17-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8082

# Run the application
CMD ["java", "-jar", "app.jar"]
