package ru.otus.spring.hw26.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.hw26.book.config.ControllerConfigTest;
import ru.otus.spring.hw26.book.data.AuthorDtoDataBuilder;
import ru.otus.spring.hw26.book.dto.AuthorDto;
import ru.otus.spring.hw26.book.exception.ServiceException;
import ru.otus.spring.hw26.book.dto.AuthorSearch;
import ru.otus.spring.hw26.book.service.CrudService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration(classes = {ControllerConfigTest.class})
@ExtendWith(SpringExtension.class)
class AuthorControllerTest {
    private static final String FIRSTNAME = "Александр";
    private static final String LASTNAME = "Пушкин";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CrudService<AuthorDto, AuthorSearch> authorService;

    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldCorrectSaveNewAuthor() throws Exception {
        AuthorDto author = AuthorDtoDataBuilder.author().withId(null).build();
        String forSave = mapper.writeValueAsString(author);

        AuthorDto expectedAuthor = AuthorDtoDataBuilder.author().build();
        String expectedResult = mapper.writeValueAsString(expectedAuthor);

        given(authorService.save(author)).willReturn(expectedAuthor);

        mvc.perform(post("/author").contentType(APPLICATION_JSON)
                        .content(forSave))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }
    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldCorrectUpdateAuthorFirstname() throws Exception {
        AuthorDto author = AuthorDtoDataBuilder.author().build();
        String forSave = mapper.writeValueAsString(author);

        AuthorDto expectedAuthor = AuthorDtoDataBuilder.author().withFirstname("test").build();
        String expectedResult = mapper.writeValueAsString(expectedAuthor);

        given(authorService.update(author.getId(), author)).willReturn(expectedAuthor);

        mvc.perform(put("/author/{id}", 1).contentType(APPLICATION_JSON).content(forSave))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }
    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldReturnExpectedAuthors() throws Exception {
        List<AuthorDto> authors = List.of(
                AuthorDtoDataBuilder.author().withId(1L).withFirstname(FIRSTNAME).withFirstname(LASTNAME).build()
        );

        List<AuthorDto> expectedAuthors = List.of(
                AuthorDtoDataBuilder.author().withId(1L).withFirstname(FIRSTNAME).withFirstname(LASTNAME).build()
        );

        given(authorService.findByParams(AuthorSearch.builder()
                .firstname(FIRSTNAME)
                .lastname(LASTNAME)
                .build())).willReturn(authors);

        mvc.perform(get("/author").param("firstname", FIRSTNAME).param("lastname", LASTNAME))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedAuthors)));

    }
    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldCorrectDeleteAuthor() throws Exception {
        mvc.perform(delete("/author/1"))
                .andExpect(status().isNoContent());
        verify(authorService, times(1)).deleteById(1L);
    }
    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldReturnCorrectAuthorByIdI() throws Exception {
        AuthorDto expectedAuthor = AuthorDtoDataBuilder.author().withFirstname("test").build();
        given(authorService.findById(expectedAuthor.getId())).willReturn(expectedAuthor);

        mvc.perform(get("/author/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedAuthor)));
    }
    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldReturnExpectedErrorWhenAuthorNotFound() throws Exception {
        given(authorService.findById(1L)).willThrow(new ServiceException("exception.object-not-found"));

        mvc.perform(get("/author/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("exception.object-not-found"));
    }

}