FROM eclipse-temurin:17-jre

WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} sales-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "sales-service.jar"]