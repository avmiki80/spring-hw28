package ru.otus.spring.hw26.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.hw26.book.config.ControllerConfigTest;
import ru.otus.spring.hw26.book.data.BookDTODataBuilder;
import ru.otus.spring.hw26.book.dto.BookDto;
import ru.otus.spring.hw26.book.exception.ServiceException;
import ru.otus.spring.hw26.book.dto.BookSearch;
import ru.otus.spring.hw26.book.service.CrudService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ControllerConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс BookController REST контроллер для работы с книгами")
class BookControllerTest {
    private static final String TITLE = "some title";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CrudService<BookDto, BookSearch> bookService;

    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldCorrectSaveNewBook() throws Exception {
        BookDto book = BookDTODataBuilder.book().withId(null).build();
        String forSave = mapper.writeValueAsString(book);

        BookDto expectedBook = BookDTODataBuilder.book().build();
        String expectedResult = mapper.writeValueAsString(expectedBook);

        given(bookService.save(book)).willReturn(expectedBook);

        mvc.perform(post("/book").contentType(APPLICATION_JSON)
                        .content(forSave))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldCorrectUpdateBookTitle() throws Exception {
        BookDto book = BookDTODataBuilder.book().build();
        String forSave = mapper.writeValueAsString(book);

        BookDto expectedBook = BookDTODataBuilder.book().withTitle("test").build();
        String expectedResult = mapper.writeValueAsString(expectedBook);

        given(bookService.update(book.getId(), book)).willReturn(expectedBook);

        mvc.perform(put("/book/{id}", 1).contentType(APPLICATION_JSON).content(forSave))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    @Test
    void shouldReturnExpectedBooks() throws Exception {
        List<BookDto> books = List.of(
                BookDTODataBuilder.book().withId(1L).withTitle(TITLE).build()
        );

        List<BookDto> expectedBooks = List.of(
                BookDTODataBuilder.book().withId(1L).withTitle(TITLE).build()
        );

        given(bookService.findByParams(BookSearch.builder()
                .title(TITLE)
                .build())).willReturn(books);

        mvc.perform(get("/book").param("title", TITLE))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBooks)));

    }

    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldCorrectDeleteBook() throws Exception {
        mvc.perform(delete("/book/1"))
                .andExpect(status().isNoContent());
        verify(bookService, times(1)).deleteById(1L);
    }

    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    @Test
    void shouldReturnCorrectBookByIdI() throws Exception {
        BookDto expectedBook = BookDTODataBuilder.book().withId(1L).withTitle(TITLE).build();
        given(bookService.findById(expectedBook.getId())).willReturn(expectedBook);

        mvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBook)));
    }

    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    @Test
    void shouldReturnExpectedErrorWhenBooktNotFound() throws Exception {
        given(bookService.findById(1L)).willThrow(new ServiceException("exception.object-not-found"));

        mvc.perform(get("/book/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("exception.object-not-found"));
    }
}