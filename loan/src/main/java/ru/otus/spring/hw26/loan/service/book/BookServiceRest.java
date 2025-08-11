package ru.otus.spring.hw26.loan.service.book;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.otus.spring.h26.model.book.BookDto;
import ru.otus.spring.hw26.loan.exception.ServiceException;

import java.util.List;

@Service
@Slf4j
@ConditionalOnProperty(name="bookservice", havingValue = "rest-template")
public class BookServiceRest implements BookService{
    private final RestTemplate restTemplate;
    private final String url;

    public BookServiceRest(
            RestTemplate restTemplate,
            @Value("${bookservice-url}") String url
    ) {
        this.restTemplate = restTemplate;
        this.url = url;
        log.debug("Book service: {}", url);
    }

    @Override
    public BookDto findById(Long id) {
        log.debug("Sending id: {}", id);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<BookDto> response = restTemplate.exchange(
                    url + "/book/" + id,
                    HttpMethod.GET,
                    entity,
                    BookDto.class
            );
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
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(url + "/book")
                    .queryParam("title", title)
                    .queryParam("genre", genre)
                    .queryParam("firstname", firstname)
                    .queryParam("lastname", lastname);
            HttpEntity<String> entity = new HttpEntity<>(headers);

// Отправка запроса и получение коллекции
            ResponseEntity<List<BookDto>> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ServiceException("BookService service returned status: " + response.getStatusCode());
            }
            return response.getBody();
        } catch (Exception e) {
            throw new ServiceException("Failed send : " +  e.getMessage());
        }
    }
}
