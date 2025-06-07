FROM maven:3.6.3-openjdk-11-slim AS build

WORKDIR /app
COPY ../pom.xml .
COPY moderator/pom.xml ./moderator/pom.xml
COPY model/pom.xml ./model/pom.xml
# костыль, чтоб собрка не падала из-за не найденого модуля
COPY book/pom.xml ./book/pom.xml

COPY model/src ./model/src
RUN mvn clean install -pl model -am -Dmaven.test.skip=true -DskipTests

RUN mvn dependency:resolve

COPY moderator/src ./moderator/src
RUN mvn clean install -pl moderator -am -Dmaven.test.skip=true -DskipTests

FROM openjdk:11-jdk-slim

WORKDIR /app

COPY --from=build /app/moderator/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]