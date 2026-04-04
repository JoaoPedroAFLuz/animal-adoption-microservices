FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY shared/pom.xml shared/pom.xml
COPY shared/src shared/src
RUN cd shared && mvn install -DskipTests -q

COPY notification-service/pom.xml notification-service/pom.xml
COPY notification-service/src notification-service/src
RUN cd notification-service && mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/notification-service/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
