FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY api-gateway/build/libs/api-gateway.jar api-gateway.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]