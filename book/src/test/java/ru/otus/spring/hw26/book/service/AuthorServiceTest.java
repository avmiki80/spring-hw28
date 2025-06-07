package ru.otus.spring.hw26.book.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.hw26.book.config.ConfigTest;
import ru.otus.spring.hw26.book.data.AuthorDtoDataBuilder;
import ru.otus.spring.hw26.book.dto.AuthorDto;
import ru.otus.spring.hw26.book.exception.ServiceException;
import ru.otus.spring.hw26.book.dto.AuthorSearch;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
@ContextConfiguration(classes = ConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс AuthorService для работы с авторами")
class AuthorServiceTest {
    private static final int EXPECTED_AUTHORS_COUNT = 2;
    private static final long EXISTING_AUTHOR_ID = 1L;
    private static final long UNEXISTING_AUTHOR_ID = 3000L;
    private static final String EXISTING_AUTHOR_LASTNAME = "Пушкин";
    private static final String EXISTING_AUTHOR_FIRSTNAME = "Александр";

    @Autowired
    private CrudService<AuthorDto, AuthorSearch> authorService;
    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(authorService).isNotNull();
    }
    @DisplayName("Получение всех авторов")
    @Test
    public void shouldReturnAllAuthors(){
        AuthorDto expectedAuthor = AuthorDtoDataBuilder.author().withId(EXISTING_AUTHOR_ID).withFirstname(EXISTING_AUTHOR_FIRSTNAME).withLastname(EXISTING_AUTHOR_LASTNAME).build();
        List<AuthorDto> authors = authorService.findAll();
        assertAll(
                () -> assertThat(authors.size()).isEqualTo(EXPECTED_AUTHORS_COUNT),
                () -> assertThat(authors).usingFieldByFieldElementComparator()
                        .contains(expectedAuthor)
        );
    }

    @DisplayName("Поиск авторов по имени и фамилии")
    @Test
    public void whenFindByLastnameAndFirstname_thenShouldReturnFilteredAuthors(){
        AuthorDto expectedAuthor = AuthorDtoDataBuilder.author().withId(EXISTING_AUTHOR_ID).withFirstname(EXISTING_AUTHOR_FIRSTNAME).withLastname(EXISTING_AUTHOR_LASTNAME).build();
        assertAll(
                () -> assertThat(authorService.findByParams(
                        AuthorSearch.builder()
                                .firstname(EXISTING_AUTHOR_FIRSTNAME)
                                .lastname(EXISTING_AUTHOR_LASTNAME)
                                .build()))
                        .usingFieldByFieldElementComparator()
                        .contains(expectedAuthor)
        );
    }

    @DisplayName("Сохрание автора")
    @Test
    public void whenSaveAuthor_thenCorrectResult(){
        AuthorDto expectedAuthor = AuthorDtoDataBuilder.author().build();
        AuthorDto actualAuthor= authorService.save(expectedAuthor);
        assertAll(
                () -> assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor)
        );
    }

    @DisplayName("Изменение автора")
    @Test
    public void whenUpdateAuthor_thenCorrectResult(){
        AuthorDto expectedAuthor = AuthorDtoDataBuilder.author().withFirstname("test").build();
        AuthorDto actualAuthor = authorService.update(EXISTING_AUTHOR_ID, expectedAuthor);
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Получение автора по ID")
    @Test
    public void whenGetAuthor_thenCorrectResult(){
        AuthorDto expectedAuthor = AuthorDtoDataBuilder.author().withId(EXISTING_AUTHOR_ID)
                .withFirstname(EXISTING_AUTHOR_FIRSTNAME)
                .withLastname(EXISTING_AUTHOR_LASTNAME).build();
        AuthorDto actualAuthor = authorService.findById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }
    @DisplayName("Получение автора по не существующему ID")
    @Test
    public void whenGetGenreWithUnexistedId_thenThrowsException(){
        assertThatThrownBy(() -> authorService.findById(UNEXISTING_AUTHOR_ID)).isInstanceOf(ServiceException.class);
    }
    @DisplayName("Удаление автора по ID")
    @Test
    public void whenDeleteAuthor_thenThrowsException(){
        assertAll(
                () -> assertDoesNotThrow(() -> authorService.deleteById(EXISTING_AUTHOR_ID)),
                () -> assertThatThrownBy(() -> authorService.findById(EXISTING_AUTHOR_ID)).isInstanceOf(ServiceException.class)
        );
    }
}