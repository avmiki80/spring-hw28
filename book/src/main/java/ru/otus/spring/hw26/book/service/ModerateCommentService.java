package ru.otus.spring.hw26.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.dto.CommentSearch;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModerateCommentService implements CrudService<CommentDto, CommentSearch>{
    private final ModeratorService moderatorService;
    private final CrudService<CommentDto, CommentSearch> commentService;

    @Override
    public CommentDto save(CommentDto obj) {
        CommentDto result = commentService.save(obj);
        ModerateResult moderateResult = moderatorService.toModerate(result);
        // Не важно как вызывается модерация комментариев через REST или кафка, логика обработки одна
        // Если модерация через кафка, то статус коментария всегда true. Изменение коментария происходит асинхронно,
        // после приема сообщения из reply топика
        if(!moderateResult.getCommentStatus()) {
            result.setText(moderateResult.getReason());
            return commentService.update(result.getId(), result);
        }
        return result;
    }

    @Override
    public CommentDto update(long id, CommentDto obj) {
        CommentDto result = commentService.update(id, obj);
        ModerateResult moderateResult = moderatorService.toModerate(result);
        // Не важно как вызывается модерация комментариев через REST или кафка, логика обработки одна
        // Если модерация через кафка, то статус коментария всегда true. Изменение коментария происходит асинхронно,
        // после приема сообщения из reply топика
        if(!moderateResult.getCommentStatus()) {
            result.setText(moderateResult.getReason());
            return commentService.update(id, result);
        }
        return result;
    }

    @Override
    public void deleteById(long id) {
        commentService.deleteById(id);
    }

    @Override
    public List<CommentDto> findAll() {
        return commentService.findAll();
    }

    @Override
    public CommentDto findById(long id) {
        return commentService.findById(id);
    }

    @Override
    public List<CommentDto> findByParams(CommentSearch params) {
        return commentService.findByParams(params);
    }
}
