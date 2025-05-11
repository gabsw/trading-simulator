FROM openjdk:21-slim

WORKDIR /app

# Copy the Maven wrapper and source code
COPY . .

# Make mvnw executable and build the project
RUN chmod +x mvnw \
    && ./mvnw clean package -DskipTests

# Copy the built JAR from target
RUN cp target/trading-simulator-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]