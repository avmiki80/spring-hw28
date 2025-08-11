package ru.otus.spring.hw26.book.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.hw26.book.config.ConfigTest;
import ru.otus.spring.hw26.book.data.CommentDataBuilder;
import ru.otus.spring.hw26.book.domain.Book;
import ru.otus.spring.hw26.book.domain.Comment;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@DisplayName("Класс CommentRepository для работы с комментариями")
@ContextConfiguration(classes = ConfigTest.class)
class CommentRepositoryTest {
    private static final int EXPECTED_COMMENT_COUNT = 7;
    private static final long EXISTING_COMMENT_ID = 1L;
    private static final String EXISTING_COMMENT_TEXT = "CommentTest1";

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TestEntityManager em;
    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(commentRepository).isNotNull();
    }
    @DisplayName("Получение всех комментариев")
    @Test
    public void shouldReturnAllComments(){
        Comment expectedComment = CommentDataBuilder.comment().withId(EXISTING_COMMENT_ID).withText(EXISTING_COMMENT_TEXT).build();
        List<Comment> comments = commentRepository.findAll();
        assertAll(
                () -> assertThat(comments.size()).isEqualTo(EXPECTED_COMMENT_COUNT),
                () -> assertThat(comments).usingElementComparatorIgnoringFields("book").contains(expectedComment)
        );
    }
    @DisplayName("Получение комментариев c фильтрацией")
    @Test
    public void shouldReturnCommentsByFilter(){
        Comment expectedComment = CommentDataBuilder.comment().withId(EXISTING_COMMENT_ID).withText(EXISTING_COMMENT_TEXT).build();
        List<Comment> comments = commentRepository.findByParams(null, "test1", null);
        assertAll(
                () -> assertThat(comments.size()).isEqualTo(3),
                () -> assertThat(comments).usingElementComparatorIgnoringFields("book").contains(expectedComment)
        );
    }

    @DisplayName("Сохрание комментария")
    @Test
    public void whenSaveComment_thenCorrectResult(){        ;
        Comment expectedComment = CommentDataBuilder.comment()
                .withId(null)
                .withText("test")
                .withBook(em.find(Book.class, 1L))
                .build();
        Comment actualComment = commentRepository.save(expectedComment);
        assertAll(
                () -> assertThat(actualComment).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedComment)
        );
    }

    @DisplayName("Изменение комментария")
    @Test
    public void whenUpdateComment_thenCorrectResult(){
        Comment expectedComment = CommentDataBuilder.comment()
                .withId(1L)
                .withText("test")
                .withBook(em.find(Book.class, 1L))
                .build();
        Comment actualComment = commentRepository.save(expectedComment);
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("Получение комментария по ID")
    @Test
    public void whenGetComment_thenCorrectResult(){
        Comment expectedComment = CommentDataBuilder.comment().withId(EXISTING_COMMENT_ID).withText(EXISTING_COMMENT_TEXT).build();
        Comment actualComment = commentRepository.findById(expectedComment.getId()).get();
        assertThat(actualComment).usingRecursiveComparison().ignoringFields("book").isEqualTo(expectedComment);
    }
    @DisplayName("Удаление комментария по ID")
    @Test
    public void whenDeleteComment_thenThrowsExceptionIfGetBookAgain(){
        assertAll(
                () -> assertDoesNotThrow(() -> commentRepository.deleteById(EXISTING_COMMENT_ID)),
                () -> assertThat(commentRepository.findById(EXISTING_COMMENT_ID)).isEqualTo(Optional.empty())
        );
    }

    @DisplayName("Получение комментария по ИД книги")
    @Test
    public void shouldReturnCommentsByBookTitle(){
        Comment expectedComment = CommentDataBuilder.comment().withId(EXISTING_COMMENT_ID).withText(EXISTING_COMMENT_TEXT).build();
        List<Comment> comments = commentRepository.findByParams(null, null, Collections.singleton(1L));
        assertAll(
                () -> assertThat(comments.size()).isEqualTo(3),
                () -> assertThat(comments).usingElementComparatorIgnoringFields("book").contains(expectedComment)
        );
    }
}