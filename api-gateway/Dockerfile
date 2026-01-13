FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} api-gateway.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]