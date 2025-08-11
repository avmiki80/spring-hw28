package ru.otus.spring.hw26.moderator.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.h26.model.mailsender.EmailDto;

@FeignClient(name = "mailsender", fallback = MailSenderFeignFallback.class, configuration = OAuth2FeignConfig.class)
public interface MailSenderFeignClient {
    @CircuitBreaker(name = "mailSenderService")
    // задал retry в учебных целях,
    // Есть понимание, что для не идемпотентных методов так лучше не делать.
    @Retry(name = "mailSenderService")
    @PostMapping(value = "/send")
    ResponseEntity<Void> send(@RequestBody EmailDto emailDto);
}
