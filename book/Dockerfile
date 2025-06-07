FROM maven:3.6.3-openjdk-11-slim AS build

WORKDIR /app
COPY ../pom.xml .
COPY book/pom.xml ./book/pom.xml
COPY model/pom.xml ./model/pom.xml
# костыль, чтоб собрка не падала из-за не найденого модуля
COPY moderator/pom.xml ./moderator/pom.xml

COPY model/src ./model/src
RUN mvn clean install -pl model -am -Dmaven.test.skip=true -DskipTests

RUN mvn dependency:resolve

COPY book/src ./book/src
RUN mvn clean install -pl book -am -Dmaven.test.skip=true-DskipTests

FROM openjdk:11-jdk-slim

WORKDIR /app

COPY --from=build /app/book/target/*.jar app.jar

#ADD https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.20.0/jmx_prometheus_javaagent-0.20.0.jar /app/jmx_prometheus_javaagent.jar
#
#COPY book/target/classes/jmx_exporter.yml /app/jmx_exporter.yml

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
#ENTRYPOINT ["java", "-javaagent:/app/jmx_prometheus_javaagent.jar=9090:/app/jmx_exporter.yml", "-jar", "/app/app.jar"]