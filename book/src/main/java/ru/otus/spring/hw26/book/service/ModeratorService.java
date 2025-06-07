package ru.otus.spring.hw26.book.service;

import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.hw26.book.dto.CommentDto;

public interface ModeratorService {
    ModerateResult toModerate(CommentDto commentDto);
}
