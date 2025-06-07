package ru.otus.spring.hw26.book.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw26.book.domain.Book;
import ru.otus.spring.hw26.book.domain.Comment;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.exception.ServiceException;
import ru.otus.spring.hw26.book.mapper.Mapper;
import ru.otus.spring.hw26.book.repository.BookRepository;
import ru.otus.spring.hw26.book.repository.CommentRepository;
import ru.otus.spring.hw26.book.dto.CommentSearch;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService implements CrudService<CommentDto, CommentSearch> {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final Mapper<CommentDto, Comment> commentMapper;

    @Override
    @Transactional
    public CommentDto save(CommentDto obj) {
//        log.info("Выполнение в потоке: {}", Thread.currentThread().getName());
        Book persistBook = bookRepository.findByParams(obj.getBookTitle(), null, null, null)
                .stream()
                .findAny().orElseThrow(() -> new ServiceException("exception.object-not-found"));

        Comment comment = commentMapper.toEntity(obj);
        comment.setBook(persistBook);

        Comment persistComment = commentRepository.save(comment);
        persistBook.getComments().add(persistComment);
        return commentMapper.toDto(persistComment);
    }

    @Override
    @Transactional
    public CommentDto update(long id, CommentDto obj) {
        Comment persistComment = commentRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found"));
        persistComment.setText(obj.getText());
        return commentMapper.toDto(persistComment);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            commentRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAll() {
        return commentRepository.findAll().stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto findById(long id) {
        return commentMapper.toDto(commentRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findByParams(CommentSearch params) {
        return commentRepository.findByParams(params.getText(), params.getBookTitle(), params.getBookIds()).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }
}
