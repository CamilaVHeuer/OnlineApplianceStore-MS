FROM eclipse-temurin:17-jre

WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} config-server.jar
EXPOSE 8001

ENTRYPOINT ["java", "-jar", "config-server.jar"]