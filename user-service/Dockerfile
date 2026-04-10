FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY user-service/pom.xml user-service/pom.xml
COPY user-service/src user-service/src
RUN cd user-service && mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/user-service/target/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
