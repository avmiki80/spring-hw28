package ru.otus.spring.hw26.loan.service.book;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.book.BookDto;
import ru.otus.spring.hw26.loan.exception.ServiceException;
import ru.otus.spring.hw26.loan.feign.BookFeignClient;

import java.util.List;
@Service
@Slf4j
@ConditionalOnProperty(name = "bookservice", havingValue = "feign")
public class BookServiceFeign implements BookService{
    private final BookFeignClient bookFeignClient;
    @Autowired
    public BookServiceFeign(
            @Qualifier("ru.otus.spring.hw26.loan.feign.BookFeignClient") BookFeignClient bookFeignClient) {
        this.bookFeignClient = bookFeignClient;
    }

    @Override
    public BookDto findById(Long id) {
        log.debug("Sending id: {}", id);
        try {
            ResponseEntity<BookDto> response = bookFeignClient.findById(id);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ServiceException("BookService service returned status: " + response.getStatusCode());
            }
            return response.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Failed send: " +  e.getMessage());
        }
    }

    @Override
    public List<BookDto> findByParam(String title, String genre, String firstname, String lastname) {
        log.debug("Sending title: {}, genre: {}, firstname: {}, lastname: {}", title, genre, firstname, lastname);
        try {
            ResponseEntity<List<BookDto>> response = bookFeignClient.find(title, genre, firstname, lastname);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ServiceException("BookService service returned status: " + response.getStatusCode());
            }
            return response.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Failed send: " +  e.getMessage());
        }
    }
}
