FROM eclipse-temurin:17-jre

WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} products-service.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "products-service.jar"]