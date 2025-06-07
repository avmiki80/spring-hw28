package ru.otus.spring.hw26.book.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import ru.otus.spring.hw26.book.controller.*;
import ru.otus.spring.hw26.book.dto.AuthorDto;
import ru.otus.spring.hw26.book.dto.BookDto;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.dto.GenreDto;
import ru.otus.spring.hw26.book.dto.AuthorSearch;
import ru.otus.spring.hw26.book.dto.BookSearch;
import ru.otus.spring.hw26.book.dto.CommentSearch;
import ru.otus.spring.hw26.book.dto.GenreSearch;
import ru.otus.spring.hw26.book.service.CrudService;
import ru.otus.spring.hw26.book.service.mass.MassMethodService;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@TestConfiguration
public class ControllerConfigTest {
    @MockBean
    private CrudService<AuthorDto, AuthorSearch> authorService;
    @MockBean
    private CrudService<BookDto, BookSearch> bookService;
    @MockBean
    private CrudService<CommentDto, CommentSearch> commentService;
    @MockBean
    private CrudService<GenreDto, GenreSearch> genreService;

    @MockBean
    private MassMethodService<CommentDto> massCreateComment;

    @SneakyThrows
    @Bean
    public MockMvc mvc() {
        StandaloneMockMvcBuilder mockMvcBuilder = MockMvcBuilders.standaloneSetup(
                new AuthorController(authorService),
                new BookController(bookService),
                new CommentController(commentService, massCreateComment),
                new GenreController(genreService),
                new ExceptionHandlerController());

        return mockMvcBuilder.build();
    }

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

}
