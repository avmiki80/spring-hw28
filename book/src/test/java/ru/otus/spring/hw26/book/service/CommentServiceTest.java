package ru.otus.spring.hw26.book.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.hw26.book.config.ConfigTest;
import ru.otus.spring.hw26.book.data.CommentDtoDataBuilder;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.exception.ServiceException;
import ru.otus.spring.hw26.book.dto.CommentSearch;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@ContextConfiguration(classes = ConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс CommentService для работы с комментариями")
class CommentServiceTest {
    private static final int EXPECTED_COMMENT_COUNT = 7;
    private static final long EXISTING_COMMENT_ID = 1L;
    private static final String EXISTING_COMMENT_TEXT = "CommentTest1";
    @Autowired
    private CrudService<CommentDto, CommentSearch> commentService;

    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(commentService).isNotNull();
    }
    @DisplayName("Получение всех комментариев")
    @Test
    public void shouldReturnAllComments(){
        CommentDto expectedComment = CommentDtoDataBuilder.comment().build();
        List<CommentDto> comments = commentService.findAll();
        assertAll(
                () -> assertThat(comments.size()).isEqualTo(EXPECTED_COMMENT_COUNT),
                () -> assertThat(comments).usingFieldByFieldElementComparator()
                        .contains(expectedComment)
        );
    }
    @DisplayName("Получение комментариев c фильтрацией")
    @Test
    public void shouldReturnCommentsByFilter(){
        CommentDto expectedComment = CommentDtoDataBuilder.comment().withId(EXISTING_COMMENT_ID).withText(EXISTING_COMMENT_TEXT).build();
        List<CommentDto> comments = commentService.findByParams(CommentSearch.builder().bookTitle("test1").build());
        assertAll(
                () -> assertThat(comments.size()).isEqualTo(3),
                () -> assertThat(comments).usingElementComparatorIgnoringFields("book").contains(expectedComment)
        );
    }

    @DisplayName("Сохрание комментария")
    @Test
    public void whenSaveComment_thenCorrectResult(){
        CommentDto expectedComment = CommentDtoDataBuilder.comment().withText("Test").build();
        CommentDto actualComment = commentService.save(expectedComment);
        assertAll(
                () -> assertThat(actualComment).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedComment)
        );
    }
    @DisplayName("Сохрание комментария с несуществующей книгой")
    @Test
    public void whenSaveCommentWithWrongBook_thenThrowsException(){
        assertThatThrownBy(() -> commentService.save(CommentDtoDataBuilder.comment()
                .withText("Test")
                .withBookTitle("qqqq")
                .build())).isInstanceOf(ServiceException.class);
    }

    @DisplayName("Изменение комментария")
    @Test
    public void whenUpdateComment_thenCorrectResult(){
        CommentDto expectedComment = CommentDtoDataBuilder.comment().withText("Test").build();
        CommentDto actualComment = commentService.update(EXISTING_COMMENT_ID, expectedComment);
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("Получение комментария по ID")
    @Test
    public void whenGetComment_thenCorrectResult(){
        CommentDto expectedComment = CommentDtoDataBuilder.comment().withId(EXISTING_COMMENT_ID).build();
        CommentDto actualComment = commentService.findById(expectedComment.getId());
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }
    @DisplayName("Удаление комментария по ID")
    @Test
    public void whenDeleteComment_thenThrowsExceptionIfGetBookAgain(){
        assertAll(
                () -> assertDoesNotThrow(() -> commentService.deleteById(EXISTING_COMMENT_ID)),
                () -> assertThatThrownBy(() -> commentService.findById(EXISTING_COMMENT_ID)).isInstanceOf(ServiceException.class)
        );
    }
    @DisplayName("Получение комментария по ИД книги")
    @Test
    public void shouldReturnCommentsByBookTitle(){
        CommentDto expectedComment = CommentDtoDataBuilder.comment().withId(EXISTING_COMMENT_ID).withText(EXISTING_COMMENT_TEXT).build();
        List<CommentDto> comments = commentService.findByParams(
                CommentSearch.builder()
                        .bookIds(Collections.singleton(1L))
                        .build());
        assertAll(
                () -> assertThat(comments.size()).isEqualTo(3),
                () -> assertThat(comments).usingElementComparatorIgnoringFields("book").contains(expectedComment)
        );
    }
}