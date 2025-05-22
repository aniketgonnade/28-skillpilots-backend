# Use lightweight OpenJDK 17 base image
FROM openjdk:17-alpine

# Set working directory inside container
WORKDIR /app

# Copy the built jar file from target folder
COPY target/*.jar app.jar

# Expose port 8081 to the outside world
EXPOSE 8081

# Run the jar file, set server port to 8081
CMD ["java", "-jar", "app.jar", "--server.port=8081"]

RUN echo "Hello my name is Aniket"
