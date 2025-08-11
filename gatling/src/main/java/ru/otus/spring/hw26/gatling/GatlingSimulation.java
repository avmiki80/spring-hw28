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
            .authorizationHeader("Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJJX0trTl92aTNqWERWdEdkSGdQYndidEN1aGhxLTg2aVFhRDFGWnpuaWhFIn0.eyJleHAiOjE3NTQ4OTM0MTAsImlhdCI6MTc1NDg4OTgxMCwianRpIjoib25ydHJvOjIzOGI2YWU4LWE2MjUtNDE2Mi04ODU4LThiYTUxZmJjMTg5YSIsImlzcyI6Imh0dHA6Ly9rZXljbG9hazo4MDgwL3JlYWxtcy9saWJyYXJ5IiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImFiY2Q3YzgwLTg5YjMtNDhhMC04M2U4LTI2Mzc0YWU4MDQ2ZiIsInR5cCI6IkJlYXJlciIsImF6cCI6ImxpYnJhcnkiLCJzaWQiOiJhYmI4MDBiOC1hMzBjLTQxZWUtOTNkNi04ZTlkM2VkZmY3MjIiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLWxpYnJhcnkiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImxpYnJhcnkiOnsicm9sZXMiOlsiQURNSU4iLCJVU0VSIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiYWRtaW4gYWRtaW4iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbiIsImdpdmVuX25hbWUiOiJhZG1pbiIsImZhbWlseV9uYW1lIjoiYWRtaW4iLCJlbWFpbCI6ImFkbWluQG1haWwucnUifQ.deJ-lqd_iL10xvcSxs5eP-TWWFvypJuTw7pZPg_u54oyYLTwjM1YZGllAgxmIzIfPUx2LpU40DzwaZwC11Pn0pUAVTIOcLRo711I18cAG5LB90v_83mmT78oI83Qnx-_yoZMnccak7XFC6PWXpvt1SdNW3pzDrmUpRyC6bxnFXA2HQsD7mN35wi7h93gqcqQaiZxyot5we8vKwVb2xbh4jInTdzozH5LmSCz8MDDc247pB-CUSarODjgiQE4JEXTsYhuCN76jz5fKtDLMbwEcbiIC1zcFEczEaE8QvcFVAlmAH4VtrGQ2HWy2kCQJTo1H0bjBp-dJ1nRG-fwYiUDhg");

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
