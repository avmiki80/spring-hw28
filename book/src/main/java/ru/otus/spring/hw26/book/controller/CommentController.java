package ru.otus.spring.hw26.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.dto.CommentSearch;
import ru.otus.spring.hw26.book.service.CrudService;
import ru.otus.spring.hw26.book.service.mass.MassMethodService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment API", description = "API комментариев")
public class CommentController {
    private final CrudService<CommentDto, CommentSearch> commentUserService;
    private final MassMethodService<CommentDto> userCommentMassMethodService;
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Добавить комментарии", description = "Массово создает комментарии")
    @PostMapping("/comments")
    public ResponseEntity<List<CommentDto>> massSave(@RequestBody List<CommentDto> comments){
        return ResponseEntity.ok(userCommentMassMethodService.massCreate(comments));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Изменить комментарии", description = "Массово изменяет комментарии")
    @PutMapping("/comments")
    public ResponseEntity<List<CommentDto>> massUpdate(@RequestBody List<CommentDto> comments){
        return ResponseEntity.ok(userCommentMassMethodService.massUpdate(comments));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Удалить комментарии", description = "Массово удаляет комментарии")
    @DeleteMapping("/comments")
    public ResponseEntity<Void> massDelete(@RequestBody List<Long> ids){
        userCommentMassMethodService.massDelete(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Добавить комментарий", description = "Создает комментарий")
    @PostMapping("/comment")
    public ResponseEntity<CommentDto> save(@RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentUserService.save(commentDto));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Изменить комментарий", description = "Изменяет комментарий")
    @PutMapping("/comment/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable(name = "id") Long id, @RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentUserService.update(id, commentDto));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Удалить комментарий", description = "Удаляет комментарий")
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        commentUserService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Получить комментарий по Id", description = "Возвращает комментарий по Id")
    @GetMapping("/comment/{id}")
    public ResponseEntity<CommentDto> findById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(commentUserService.findById(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Поиск комментариев", description = "Возвращает коллекцию комментариев")
    @GetMapping("/comment")
    public ResponseEntity<List<CommentDto>> find(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "bookTitle", required = false) String bookTitle
    ){
        return ResponseEntity.ok(commentUserService.findByParams(CommentSearch.builder()
                .text(text)
                .bookTitle(bookTitle)
                .build()));
    }
}
