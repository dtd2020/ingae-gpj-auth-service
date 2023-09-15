# Use a JDK 17 base image
FROM azul/zulu-openjdk:17

# Set the working directory to /app
WORKDIR /app

# Copy the Maven configuration files (pom.xml) to the container
COPY pom.xml .

# Copy the rest of the application files to the container
COPY src/ src/

# Build the application using Maven
RUN mvn clean install -DskipTests

# Start the application
CMD ["java", "-jar", "target/gpj-auth-service-0.0.1-SNAPSHOT.jar"]

