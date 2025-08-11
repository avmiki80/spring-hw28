FROM maven:3.6.3-openjdk-11-slim AS build

WORKDIR /app
COPY gateway/pom.xml ./pom.xml

RUN mvn dependency:resolve

COPY gateway/src ./src
RUN mvn clean install -Dmaven.test.skip=true -DskipTests

FROM openjdk:11-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

#EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]