
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/shopjava_forestage_backend-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"] 