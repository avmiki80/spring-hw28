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
import ru.otus.spring.hw26.book.data.GenreDtoDataBuilder;
import ru.otus.spring.hw26.book.dto.GenreDto;
import ru.otus.spring.hw26.book.exception.ServiceException;
import ru.otus.spring.hw26.book.dto.GenreSearch;
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
class GenreControllerTest {
    private static final String TITLE = "title";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CrudService<GenreDto, GenreSearch> genreService;
    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldCorrectSaveNewGenre() throws Exception {
        GenreDto genre = GenreDtoDataBuilder.genre().withId(null).build();
        String forSave = mapper.writeValueAsString(genre);

        GenreDto expectedGenre = GenreDtoDataBuilder.genre().build();
        String expectedResult = mapper.writeValueAsString(expectedGenre);

        given(genreService.save(genre)).willReturn(expectedGenre);

        mvc.perform(post("/genre").contentType(APPLICATION_JSON)
                        .content(forSave))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }
    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldCorrectUpdateGenreTitle() throws Exception {
        GenreDto genre = GenreDtoDataBuilder.genre().build();
        String forSave = mapper.writeValueAsString(genre);

        GenreDto expectedGenre = GenreDtoDataBuilder.genre().withTitle("test").build();
        String expectedResult = mapper.writeValueAsString(expectedGenre);

        given(genreService.update(genre.getId(), genre)).willReturn(expectedGenre);

        mvc.perform(put("/genre/{id}", 1).contentType(APPLICATION_JSON).content(forSave))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }
    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldReturnExpectedGenres() throws Exception {
        List<GenreDto> genres = List.of(
                GenreDtoDataBuilder.genre().withId(1L).withTitle(TITLE).build()
        );

        List<GenreDto> expectedGenres = List.of(
                GenreDtoDataBuilder.genre().withId(1L).withTitle(TITLE).build()
        );

        given(genreService.findByParams(GenreSearch.builder()
                .title(TITLE)
                .build())).willReturn(genres);

        mvc.perform(get("/genre").param("title", TITLE))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedGenres)));

    }
    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldCorrectDeleteGenre() throws Exception {
        mvc.perform(delete("/genre/1"))
                .andExpect(status().isNoContent());
        verify(genreService, times(1)).deleteById(1L);
    }
    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldReturnCorrectGenreByIdI() throws Exception {
        GenreDto expectedGenre = GenreDtoDataBuilder.genre().withTitle("test").build();
        given(genreService.findById(expectedGenre.getId())).willReturn(expectedGenre);

        mvc.perform(get("/genre/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedGenre)));
    }
    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldReturnExpectedErrorWhenGenreNotFound() throws Exception {
        given(genreService.findById(1L)).willThrow(new ServiceException("exception.object-not-found"));

        mvc.perform(get("/genre/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("exception.object-not-found"));
    }
}