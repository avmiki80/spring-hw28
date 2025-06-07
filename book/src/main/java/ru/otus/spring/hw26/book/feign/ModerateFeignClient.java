package ru.otus.spring.hw26.book.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.hw26.book.dto.CommentDto;

@FeignClient(name = "moderator-service", fallback = ModerateFeignFallback.class)
public interface ModerateFeignClient {
    @CircuitBreaker(name = "moderatorService")
    // задал retry в учебных целях,
    // Есть понимание, что для не идемпотентных методов так лучше не делать.
    @Retry(name = "moderatorService")
    @PostMapping(value = "/moderator")
    ResponseEntity<ModerateResult> moderate(@RequestBody CommentDto comment);
}
