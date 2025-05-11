FROM openjdk:21-slim

WORKDIR /app

# Copy the Maven wrapper and source code
COPY . .

# Make the wrapper script executable
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Run the client
CMD ["./mvnw", "exec:java"]
