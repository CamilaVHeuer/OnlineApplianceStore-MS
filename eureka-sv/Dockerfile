FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} eureka-sv.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eureka-sv.jar"]