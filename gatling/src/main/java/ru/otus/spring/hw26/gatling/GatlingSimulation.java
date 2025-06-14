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
            .authorizationHeader("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJJX0trTl92aTNqWERWdEdkSGdQYndidEN1aGhxLTg2aVFhRDFGWnpuaWhFIn0.eyJleHAiOjE3NDk4ODYyMjMsImlhdCI6MTc0OTg4MjYyMywianRpIjoib25ydHJvOmNjZGQ0YjEzLTcwMzMtNDcxNC04ZDVjLTAwM2IyNTgyODcyYiIsImlzcyI6Imh0dHA6Ly9rZXljbG9hazo4MDgwL3JlYWxtcy9saWJyYXJ5IiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImFiY2Q3YzgwLTg5YjMtNDhhMC04M2U4LTI2Mzc0YWU4MDQ2ZiIsInR5cCI6IkJlYXJlciIsImF6cCI6ImxpYnJhcnkiLCJzaWQiOiIyZjM2YWU5MC1iYzRjLTQ3M2MtYmM1OC1jZmI0MmFmMWY2YzQiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLWxpYnJhcnkiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImxpYnJhcnkiOnsicm9sZXMiOlsiQURNSU4iLCJVU0VSIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiYWRtaW4gYWRtaW4iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbiIsImdpdmVuX25hbWUiOiJhZG1pbiIsImZhbWlseV9uYW1lIjoiYWRtaW4iLCJlbWFpbCI6ImFkbWluQG1haWwucnUifQ.h_PmHkrXvZNML6JDXK42D2adW9lZSkLOntycupJK5SBZzDjz700z7DYRVTtWkKunGIXGsm5aPxaKhWcYVFNXbiNk256AE3l1iYQlof2ovQi-XZDdBCYzeDcZzyGy5GYZOEVug5AnUmy6l-l6kWkA0blIEI0M2g5z7kRDqz1RWYvLBQogffu_RD69V_ZbjHyLGp6Awj9OSKXUAkQ4jY0fGYfeRYMlWjwNdQp7jMeP9Ez_h5U2k6uxJR8yL7EjyVrAd6IpJLRPafaNNLcymBbjT2nalkNygxo4S3nsuj0cl05uzdUWe7CKlJk5WMYRcImjJqxiMU78oF5boUL0-L5tmA");

    // Генерация JSON-массива коментариев
    ChainBuilder generateComments =
            exec(session -> {
                String commentsJson = IntStream.rangeClosed(1, 3000)
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
//                        nothingFor(5), // Ничего не делать 5 секунд
                        constantUsersPerSec(5).during(30), // Поддерживать 5 запросов/сек в течение 30 секунд
//                        nothingFor(5), // Ничего не делать 5 секунд
                        rampUsersPerSec(5).to(20).during(20) // Увеличивать с 5 до 20 запросов/сек за 20 секунд
                )
        ).protocols(httpProtocol)
                .maxDuration(Duration.ofMinutes(5));
    }
}
