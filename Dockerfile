# Stage 1: build
FROM arm64v8/eclipse-temurin:17-jdk AS builder

WORKDIR /app
COPY . .
RUN chmod +x mvnw \
 && ./mvnw clean package -DskipTests

# Stage 2: runtime
FROM arm64v8/eclipse-temurin:17-jre

WORKDIR /app

# Copie doar jar-ul compilat
COPY --from=builder /app/target/feet-flow-api-0.0.1-SNAPSHOT.jar app.jar

# Expune portul
EXPOSE 8080

# Rulează aplicația cu profilul "prod"
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
