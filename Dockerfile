# Etapa de construcción
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/torneo-verano-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx384m","-jar","app.jar"]