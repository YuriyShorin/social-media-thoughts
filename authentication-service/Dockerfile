FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app
COPY authentication-service/build/libs/authentication-service.jar authentication-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "authentication-service.jar"]