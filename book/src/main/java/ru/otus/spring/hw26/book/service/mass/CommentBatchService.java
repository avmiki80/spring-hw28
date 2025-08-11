package ru.otus.spring.hw26.book.service.mass;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw26.book.domain.Book;
import ru.otus.spring.hw26.book.domain.Comment;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.exception.ServiceException;
import ru.otus.spring.hw26.book.mapper.Mapper;
import ru.otus.spring.hw26.book.repository.BookRepository;
import ru.otus.spring.hw26.book.repository.CommentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentBatchService implements BatchService<CommentDto> {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final Mapper<CommentDto, Comment> commentMapper;

    // Не уверен насчет Retryable, сделал так из-за того что пачку желательно сохранить, хотя бы несколько раз попытаться,
    // хотя можно соединения с БД исчерпать или БД излишне нагрузить.
    // Можно к value ServiceException и SQLException добавить, для надежности
    @Transactional
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(multiplier=2.0, maxDelay = 5000, delay = 1000)
    )
    public List<CommentDto> batchSave(List<CommentDto> objs){

        Map<String, Book> persistBooks = bookRepository.findByTitleIn(
                objs.stream()
                        .map(CommentDto::getBookTitle)
                        .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(Book::getTitle, Function.identity()));

        List<Comment> comments = objs.stream()
                .map(commentMapper::toEntity)
                .peek(c -> {
                    Book book = persistBooks.get(c.getBook().getTitle());
                    if (book == null) {
                        throw new ServiceException("Book not found: " + c.getBook().getTitle());
                    }
                    c.setBook(book);
                })
                .collect(Collectors.toList());

        List<Comment> persistComments = commentRepository.saveAll(comments);
        persistComments.forEach(c -> persistBooks.get(c.getBook().getTitle()).getComments().add(c));
        return persistComments.stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Recover
    public List<CommentDto> batchSave(RuntimeException e, List<CommentDto> objs) {
        log.debug(e.getMessage());
        return Collections.emptyList();
    }

    @Override
    @Transactional
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(multiplier=2.0, maxDelay = 5000, delay = 1000)
    )
    public List<CommentDto> batchUpdate(List<CommentDto> objs) {
        Set<Long> ids = objs.stream().map(CommentDto::getId).collect(Collectors.toSet());
        List<Comment> persistComments = commentRepository.findAllById(ids);
        Map<Long, Comment> persistMap = persistComments.stream().collect(Collectors.toMap(Comment::getId, Function.identity()));
        objs.forEach(c -> {
            if(persistMap.get(c.getId()) == null)
                throw new ServiceException("exception.object-not-found " + c.getId());
            persistMap.get(c.getId()).setText(c.getText());

        });
        return persistComments.stream().map(commentMapper::toDto).collect(Collectors.toList());
    }
    @Recover
    public List<CommentDto> batchUpdate(RuntimeException e, List<CommentDto> objs) {
        log.debug(e.getMessage());
        return Collections.emptyList();
    }

    @Override
    @Transactional
    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(multiplier=2.0, maxDelay = 5000, delay = 1000)
    )
    public void batchDelete(List<Long> ids) {
        try {
            commentRepository.deleteAllByIdInBatch(ids);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Recover
    public void batchDelete(RuntimeException e, List<Long> ids) {
        log.debug(e.getMessage());
    }
}
