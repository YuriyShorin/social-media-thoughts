FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY eureka-service/build/libs/eureka-service.jar eureka-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "eureka-service.jar"]