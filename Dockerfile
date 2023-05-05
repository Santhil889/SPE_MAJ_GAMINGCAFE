# Use the OpenJDK 11 image as the base image
#FROM openjdk:11-jdk --platform linux/arm64/v8
FROM openjdk:11-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the application files from the host machine to the container
COPY target/noidea2-0.0.1-SNAPSHOT.jar /app/noidea2-0.0.1-SNAPSHOT.jar

# Expose the port that the application will listen on
EXPOSE 8081

# Specify the command to run when the container starts
CMD ["java", "-jar", "noidea2-0.0.1-SNAPSHOT.jar"]