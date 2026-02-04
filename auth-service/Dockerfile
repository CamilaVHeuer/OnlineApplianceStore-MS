FROM eclipse-temurin:17-jre

WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} auth-service.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "auth-service.jar"]