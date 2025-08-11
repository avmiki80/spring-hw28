package ru.otus.spring.hw26.loan.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.otus.spring.h26.model.book.BookDto;

import java.util.Collections;
import java.util.List;
@Component
@Slf4j
public class BookFeignFallback implements BookFeignClient{
    private final BookDto book = BookDto.builder().id(0L).build();
    @Override
    public ResponseEntity<BookDto> findById(Long id) {
        return ResponseEntity.ok(book);
    }

    @Override
    public ResponseEntity<List<BookDto>> find(String title, String genre, String firstname, String lastname) {
        return ResponseEntity.ok(Collections.singletonList(book));
    }
}
