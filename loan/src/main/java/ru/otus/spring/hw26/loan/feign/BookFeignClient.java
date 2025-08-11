package ru.otus.spring.hw26.loan.feign;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.h26.model.book.BookDto;

import java.util.List;


@FeignClient(name = "book-service", fallback = BookFeignFallback.class, configuration = OAuth2FeignConfig.class)
public interface BookFeignClient {
    @CircuitBreaker(name = "bookService")
    @Retry(name = "bookService")
    @GetMapping(value = "/book/{id}")
    ResponseEntity<BookDto> findById(@PathVariable(name = "id") Long id);
    @CircuitBreaker(name = "bookService")
    @Retry(name = "bookService")
    @GetMapping("/book")
    ResponseEntity<List<BookDto>> find(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "genre", required = false) String genre,
            @RequestParam(name = "firstname", required = false) String firstname,
            @RequestParam(name = "lastname", required = false) String lastname
    );
}
