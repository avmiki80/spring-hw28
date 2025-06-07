package ru.otus.spring.hw26.moderator.service;

import ru.otus.spring.h26.model.tomoderate.Comment;
import ru.otus.spring.hw26.moderator.dto.CheckedCommentDto;

public interface ModeratorService {
    CheckedCommentDto moderate(Comment comment);
}
