FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY shared/pom.xml shared/pom.xml
COPY shared/src shared/src
RUN cd shared && mvn install -DskipTests -q

COPY pet-service/pom.xml pet-service/pom.xml
COPY pet-service/src pet-service/src
RUN cd pet-service && mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/pet-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
