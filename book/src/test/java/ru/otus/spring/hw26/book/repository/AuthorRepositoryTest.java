package ru.otus.spring.hw26.book.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.hw26.book.config.ConfigTest;
import ru.otus.spring.hw26.book.data.AuthorDataBuilder;
import ru.otus.spring.hw26.book.domain.Author;
import ru.otus.spring.hw26.book.dto.AuthorSearch;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ConfigTest.class)
@DisplayName("Класс AuthorRepository для работы с авторами")
class AuthorRepositoryTest {
    private static final int EXPECTED_AUTHORS_COUNT = 2;
    private static final long EXISTING_AUTHOR_ID = 1L;
    private static final String EXISTING_AUTHOR_LASTNAME = "Пушкин";
    private static final String EXISTING_AUTHOR_FIRSTNAME = "Александр";

    @Autowired
    private AuthorRepository authorRepository;
    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertAll(
                () -> assertThat(authorRepository).isNotNull()
        );
    }
    @DisplayName("Получение всех авторов")
    @Test
    public void shouldReturnAllAuthors(){
        Author expectedAuthor = AuthorDataBuilder.author().withId(EXISTING_AUTHOR_ID).withFirstname(EXISTING_AUTHOR_FIRSTNAME).withLastname(EXISTING_AUTHOR_LASTNAME).build();
        List<Author> authors = authorRepository.findAll();
        assertAll(
                () -> assertThat(authors.size()).isEqualTo(EXPECTED_AUTHORS_COUNT),
                () -> assertThat(authors).usingFieldByFieldElementComparator()
                        .contains(expectedAuthor)
        );
    }

    @DisplayName("Получение авторов с фильтрацией")
    @Test
    public void shouldReturnAuthorsByFilter(){
        Author expectedAuthor = AuthorDataBuilder.author().withId(EXISTING_AUTHOR_ID).withFirstname(EXISTING_AUTHOR_FIRSTNAME).withLastname(EXISTING_AUTHOR_LASTNAME).build();
        List<Author> authors = authorRepository.findByParams(EXISTING_AUTHOR_FIRSTNAME, null);
        assertAll(
                () -> assertThat(authors.size()).isEqualTo(1),
                () -> assertThat(authors).usingFieldByFieldElementComparator()
                        .contains(expectedAuthor)
        );
    }

    @DisplayName("Сохрание автора")
    @Test
    public void whenSaveAuthor_thenCorrectResult(){
        Author expectedAuthor = AuthorDataBuilder.author().build();
        Author actualAuthor = authorRepository.save(expectedAuthor);
        assertAll(
                () -> assertThat(actualAuthor).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedAuthor)
        );
    }

    @DisplayName("Изменение автора")
    @Test
    public void whenUpdateAuthor_thenCorrectResult(){
        Author expectedAuthor = AuthorDataBuilder.author().withFirstname("test").withLastname("test").build();
        Author actualAuthor = authorRepository.save(expectedAuthor);
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Получение автора по ID")
    @Test
    public void whenGetAuthor_thenCorrectResult(){
        Author expectedAuthor = AuthorDataBuilder.author().withId(EXISTING_AUTHOR_ID)
                .withFirstname(EXISTING_AUTHOR_FIRSTNAME)
                .withLastname(EXISTING_AUTHOR_LASTNAME).build();
        Author actualAuthor = authorRepository.findById(expectedAuthor.getId()).get();
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }
    @DisplayName("Удаление автора по ID")
    @Test
    public void whenDeleteAuthor_thenThrowsExceptionIfGetAuthorAgain(){
        assertAll(
                () -> assertDoesNotThrow(() -> authorRepository.deleteById(EXISTING_AUTHOR_ID)),
                () -> assertThat(authorRepository.findById(EXISTING_AUTHOR_ID)).isEqualTo(Optional.empty())
        );
    }

    @DisplayName("Добавление автора если автор не найден")
    @Test
    public void whenAuthorNotFinded_thenSaveNewAuthor(){
        Author expectedAuthor = AuthorDataBuilder.author().withFirstname("notFinded").withLastname("notFinded").build();
        Author actualAuthor = authorRepository.findAndCreateIfAbsent(
                AuthorSearch.builder().firstname("notFinded").lastname("notFinded").build());
        assertThat(actualAuthor).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedAuthor);

    }
}