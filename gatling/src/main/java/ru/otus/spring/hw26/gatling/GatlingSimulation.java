package ru.otus.spring.hw26.gatling;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;


public class GatlingSimulation extends Simulation {

    // 1. Определение HTTP протокола
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .authorizationHeader("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJJX0trTl92aTNqWERWdEdkSGdQYndidEN1aGhxLTg2aVFhRDFGWnpuaWhFIn0.eyJleHAiOjE3NDkyOTY2NDEsImlhdCI6MTc0OTI5MzA0MSwianRpIjoib25ydHJvOjEyMGI3YmVkLWFjZDYtNDMyZi04ZGY0LTllMzVhZjUxZWQ2NSIsImlzcyI6Imh0dHA6Ly9rZXljbG9hazo4MDgwL3JlYWxtcy9saWJyYXJ5IiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjU0NWRlMTVhLWI5NTMtNGJiMy1iMGZjLTM1YTk2NjAxNmJmYSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImxpYnJhcnkiLCJzaWQiOiI4OGQwMWJlYS05ZTk2LTQzZWYtOGNjNS0xMzNjZDkzOWRjOTkiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLWxpYnJhcnkiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImxpYnJhcnkiOnsicm9sZXMiOlsiVVNFUiJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6InVzZXIgdXNlciIsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIiLCJnaXZlbl9uYW1lIjoidXNlciIsImZhbWlseV9uYW1lIjoidXNlciIsImVtYWlsIjoidXNlckBtYWlsLnJ1In0.H0LTQB9aBgs93Xbnj8NQtPVkGvuPDXGH_iX1jjv3nqmrbkMxd67Ojs8lZtmdk_5V-ZxpS2doF4QAfBIUJpuRUU38FS6Az-G4m1QJ_ETXzZEbggzavi0aw7rTh5SI1mFr5MprggRl07V62oEaFGdrLASBWqWobogoWqRSKc3PaJPaNzwB2lTZYxYoxM99mKpYRTW80M5PUQvOAXtSlD6pxLy7wMDwPZonxJhhWPoNswpGG3f-Mk7b8GlG3jRdo57pyqGzr4UT_yvHmhFNi9YeGJLztg431Aqth-q3UTJnjPLiy3sIipafy9-QpHRFl9Gc9Cz3BnKkBG1NQtZqDUwZxA");

    // Генерация JSON-массива коментариев
    ChainBuilder generateComments =
            exec(session -> {
                String commentsJson = IntStream.rangeClosed(1, 1000)
                        .mapToObj(i -> String.format(
                                "{\"text\":\"Comment%d\",\"bookTitle\":\"Book%d\"}", i, i%3))
                        .collect(Collectors.joining(",", "[", "]"));
                return session.set("commentsJson", commentsJson);
            });

    // 2. Определение сценария
    ScenarioBuilder comments =
            scenario("Mass create comments loading")
                    .exec(generateComments)
//                    .exec(
//                            http("Find comment by id")
//                                    .get("/api/comment/1")
//                                    .header("content-type", "application/json; charset=utf-8")
//                                    .requestTimeout(1200))
                    .exec(
                            http("Mass create comments")
                                    .post("/api/comments")
                                    .header("content-type", "application/json; charset=utf-8")
                                    .requestTimeout(1200)
                                    .body(StringBody("#{commentsJson}")));
//                    .exec(
//                            http("Find comment by id")
//                                    .get("/api/comment/2")
//                                    .header("content-type", "application/json; charset=utf-8")
//                                    .requestTimeout(1200));

    // 3. Настройка нагрузки
    {
        setUp(
                comments.injectOpen(
                        nothingFor(5), // Ничего не делать 5 секунд
                        rampUsers(5).during(10), // Линейно увеличивать до 5 запросов за 10 секунд
                        nothingFor(5), // Ничего не делать 5 секунд
                        constantUsersPerSec(5).during(30), // Поддерживать 5 запросов/сек в течение 30 секунд
                        nothingFor(5), // Ничего не делать 5 секунд
                        rampUsersPerSec(5).to(20).during(20) // Увеличивать с 5 до 20 запросов/сек за 20 секунд
                )
        ).protocols(httpProtocol)
                .maxDuration(Duration.ofMinutes(5));
    }
}
