package ru.otus.spring.hw26.moderator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.h26.model.tomoderate.Comment;
import ru.otus.spring.hw26.moderator.dto.CheckedCommentDto;
import ru.otus.spring.hw26.moderator.service.ModeratorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ModerateController {
    private final ModeratorService moderatorService;
    @PostMapping("/moderate")
    public ResponseEntity<CheckedCommentDto> moderate(@RequestBody Comment comment){
        return ResponseEntity.ok(moderatorService.moderate(comment));
    }
    @PostMapping("/moderates")
    public ResponseEntity<List<CheckedCommentDto>> moderate(@RequestBody List<Comment> comments){
        return ResponseEntity.ok(moderatorService.massModerate(comments));
    }
}
