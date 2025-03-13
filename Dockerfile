# Use a specific version of Maven for the build stage
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

# Copy the pom.xml and download dependencies first (to leverage Docker layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY . .
RUN mvn clean package -DskipTests

# Use a lightweight JDK for the runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]