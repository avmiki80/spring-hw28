package ru.otus.spring.hw26.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.dto.CommentSearch;
import ru.otus.spring.hw26.book.service.CrudService;
import ru.otus.spring.hw26.book.service.mass.MassMethodService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CrudService<CommentDto, CommentSearch> moderateCommentService;
    private final MassMethodService<CommentDto> moderateMassMethodCommentService;
    @PostMapping("/comments")
    public ResponseEntity<List<CommentDto>> massSave(@RequestBody List<CommentDto> comments){
        return ResponseEntity.ok(moderateMassMethodCommentService.massCreate(comments));
    }
    @PutMapping("/comments")
    public ResponseEntity<List<CommentDto>> massUpdate(@RequestBody List<CommentDto> comments){
        return ResponseEntity.ok(moderateMassMethodCommentService.massUpdate(comments));
    }
    @DeleteMapping("/comments")
    public ResponseEntity<Void> massDelete(@RequestBody List<Long> ids){
        moderateMassMethodCommentService.massDelete(ids);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentDto> save(@RequestBody CommentDto commentDto){
        return ResponseEntity.ok(moderateCommentService.save(commentDto));
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable(name = "id") Long id, @RequestBody CommentDto commentDto){
        return ResponseEntity.ok(moderateCommentService.update(id, commentDto));
    }
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        moderateCommentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/comment/{id}")
    public ResponseEntity<CommentDto> findById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(moderateCommentService.findById(id));
    }

    @GetMapping("/comment")
    public ResponseEntity<List<CommentDto>> find(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "bookTitle", required = false) String bookTitle
    ){
        return ResponseEntity.ok(moderateCommentService.findByParams(CommentSearch.builder()
                .text(text)
                .bookTitle(bookTitle)
                .build()));
    }
}
