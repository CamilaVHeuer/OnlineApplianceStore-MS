FROM eclipse-temurin:17-jre

WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} shopping-cart-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "shopping-cart-service.jar"]