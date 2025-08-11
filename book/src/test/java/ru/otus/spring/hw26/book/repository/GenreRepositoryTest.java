package ru.otus.spring.hw26.book.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.hw26.book.config.ConfigTest;
import ru.otus.spring.hw26.book.data.GenreDataBuilder;
import ru.otus.spring.hw26.book.domain.Genre;
import ru.otus.spring.hw26.book.dto.GenreSearch;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@DisplayName("Класс GenreRepository для работы с жанрами")
@ContextConfiguration(classes = ConfigTest.class)
class GenreRepositoryTest {
    private static final int EXPECTED_GENRES_COUNT = 3;
    private static final long EXISTING_GENRE_ID = 1L;
    private static final String EXISTING_GENRE_TITLE = "Фантастика";

    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertAll(
                () -> assertThat(genreRepository).isNotNull()
        );

    }
    @DisplayName("Получение всех жанров")
    @Test
    public void shouldReturnAllGenres(){
        Genre expectedGenre = GenreDataBuilder.genre().withId(EXISTING_GENRE_ID).withTitle(EXISTING_GENRE_TITLE).build();
        List<Genre> genres = genreRepository.findAll();
        assertAll(
                () -> assertThat(genres.size()).isEqualTo(EXPECTED_GENRES_COUNT),
                () -> assertThat(genres).usingFieldByFieldElementComparator()
                        .contains(expectedGenre)
        );
    }

    @DisplayName("Получение жанров с фильтрацией")
    @Test
    public void shouldReturnGenresByFilter(){
        Genre expectedGenre = GenreDataBuilder.genre().withId(EXISTING_GENRE_ID).withTitle(EXISTING_GENRE_TITLE).build();
        List<Genre> genres = genreRepository.findByParams(EXISTING_GENRE_TITLE);
        assertAll(
                () -> assertThat(genres.size()).isEqualTo(1),
                () -> assertThat(genres).usingFieldByFieldElementComparator()
                        .contains(expectedGenre)
        );
    }

    @DisplayName("Сохрание жанра")
    @Test
    public void whenSaveGenre_thenCorrectResult(){
        Genre expectedGenre = GenreDataBuilder.genre().build();
        Genre actualGenre = genreRepository.save(expectedGenre);
        assertAll(
                () -> assertThat(actualGenre).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedGenre)
        );
    }

    @DisplayName("Изменение жанра")
    @Test
    public void whenUpdateGenre_thenCorrectResult(){
        Genre expectedGenre = GenreDataBuilder.genre().withTitle("test").build();
        Genre actualGenre = genreRepository.save(expectedGenre);
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Получение жанра")
    @Test
    public void whenGetGenre_thenCorrectResult(){
        Genre expectedGenre = GenreDataBuilder.genre().withId(EXISTING_GENRE_ID).withTitle(EXISTING_GENRE_TITLE).build();
        Genre actualGenre = genreRepository.findById(expectedGenre.getId()).get();
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }
    @DisplayName("Удаление жанра по ID")
    @Test
    public void whenDeleteGenre_thenThrowsExceptionIfGetGenreAgain(){
        assertAll(
                () -> assertDoesNotThrow(() -> genreRepository.deleteById(EXISTING_GENRE_ID)),
                () -> assertThat(genreRepository.findById(EXISTING_GENRE_ID)).isEqualTo(Optional.empty())
        );
    }

    @DisplayName("Сохрание жанра если жанр не найден")
    @Test
    public void whenGenreNotFinded_thenSaveNewGenre(){
        Genre expectedGenre = GenreDataBuilder.genre().withTitle("notFinded").build();
        Genre actualGenre = genreRepository.findAndCreateIfAbsent(
                GenreSearch.builder().title("notFinded").build()
        );
        assertThat(actualGenre).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedGenre);
    }
}