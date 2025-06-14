package ru.otus.spring.hw26.book.service;

import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.hw26.book.dto.CommentDto;

import java.util.List;

public interface ModeratorService {
    ModerateResult toModerate(CommentDto commentDto);
    List<ModerateResult> toModerate(List<CommentDto> commentDto);

}
