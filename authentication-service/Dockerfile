FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY authentication-service/build/libs/authentication-service.jar authentication-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "authentication-service.jar"]