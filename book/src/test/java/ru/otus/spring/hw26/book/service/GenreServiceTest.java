package ru.otus.spring.hw26.book.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.hw26.book.config.ConfigTest;
import ru.otus.spring.hw26.book.data.GenreDtoDataBuilder;
import ru.otus.spring.hw26.book.dto.GenreDto;
import ru.otus.spring.hw26.book.exception.ServiceException;
import ru.otus.spring.hw26.book.dto.GenreSearch;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
@ContextConfiguration(classes = ConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс GenreService для работы с жанрами")
class GenreServiceTest {
    private static final int EXPECTED_GENRES_COUNT = 2;
    private static final long EXISTING_GENRE_ID = 1L;
    private static final String EXISTING_GENRE_TITLE = "Фантастика";
    private static final long UNEXISTING_GENRE_ID = 3000L;

    @Autowired
    private CrudService<GenreDto, GenreSearch> genreService;
    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(genreService).isNotNull();
    }
    @DisplayName("Получение всех жанров")
    @Test
    public void shouldReturnAllGenres(){
        GenreDto expectedGenre = GenreDtoDataBuilder.genre().withId(EXISTING_GENRE_ID).withTitle(EXISTING_GENRE_TITLE).build();
        List<GenreDto> genres = genreService.findAll();
        assertAll(
                () -> assertThat(genres.size()).isEqualTo(EXPECTED_GENRES_COUNT),
                () -> assertThat(genres).usingFieldByFieldElementComparator()
                        .contains(expectedGenre)
        );
    }
    @DisplayName("Поиск по заголовку жанра")
    @Test
    public void whenFindByTitle_shouldReturnFilteredGenres(){
        GenreDto expectedGenre = GenreDtoDataBuilder.genre().withId(EXISTING_GENRE_ID).withTitle(EXISTING_GENRE_TITLE).build();
        assertAll(
                () -> assertThat(genreService.findByParams(
                        GenreSearch.builder()
                                .title(EXISTING_GENRE_TITLE)
                                .build()))
                        .usingFieldByFieldElementComparator()
                        .contains(expectedGenre)
        );
    }

    @DisplayName("Сохрание жанра")
    @Test
    public void whenSaveGenre_thenCorrectResult(){
        GenreDto expectedGenre = GenreDtoDataBuilder.genre().build();
        GenreDto actualGenre = genreService.save(expectedGenre);
        assertAll(
                () -> assertThat(actualGenre).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedGenre)
        );
    }

    @DisplayName("Изменение жанра")
    @Test
    public void whenUpdateGenre_thenCorrectResult(){
        GenreDto expectedGenre = GenreDtoDataBuilder.genre().withId(EXISTING_GENRE_ID).withTitle("test").build();
        GenreDto actualGenre = genreService.update(EXISTING_GENRE_ID, expectedGenre);
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Получение жанра")
    @Test
    public void whenGetGenre_thenCorrectResult(){
        GenreDto expectedGenre = GenreDtoDataBuilder.genre().withId(EXISTING_GENRE_ID).withTitle(EXISTING_GENRE_TITLE).build();
        GenreDto actualGenre = genreService.findById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }
    @DisplayName("Получение жанра по не существующему ID")
    @Test
    public void whenGetGenreWithUnexistedId_thenThrowsException(){
        assertThatThrownBy(() -> genreService.findById(UNEXISTING_GENRE_ID)).isInstanceOf(ServiceException.class);
    }
    @DisplayName("Удаление жанра по ID")
    @Test
    public void whenDeleteGenre_thenThrowsExceptionIfGetGenreAgain(){
        assertAll(
                () -> assertDoesNotThrow(() -> genreService.deleteById(EXISTING_GENRE_ID)),
                () -> assertThatThrownBy(() -> genreService.findById(EXISTING_GENRE_ID)).isInstanceOf(ServiceException.class)
        );
    }
}