package ru.otus.spring.hw26.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw26.book.dto.BookDto;
import ru.otus.spring.hw26.book.dto.BookSearch;
import ru.otus.spring.hw26.book.service.CrudService;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
@Tag(name = "Book API", description = "API книг")
public class BookController {
    private final CrudService<BookDto, BookSearch> bookService;
    @Operation(summary = "Добавить книгу", description = "Создает книгу")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<BookDto> save(@RequestBody BookDto bookDto){
        return ResponseEntity.ok(bookService.save(bookDto));
    }
    @Operation(summary = "Изменить книгу", description = "Изменяет книгу")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> update(@PathVariable(name = "id") Long id, @RequestBody BookDto bookDto){
        return ResponseEntity.ok(bookService.update(id, bookDto));
    }
    @Operation(summary = "Удалить книгу", description = "Удаляет книгу")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        bookService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Получить книгу по Id", description = "Возвращает книгу по Id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(bookService.findById(id));
    }
    @Operation(summary = "Поиск книг", description = "Возвращает коллекцию книг")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping()
    public ResponseEntity<List<BookDto>> find(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "genre", required = false) String genre,
            @RequestParam(name = "firstname", required = false) String firstname,
            @RequestParam(name = "lastname", required = false) String lastname
    ){
        return ResponseEntity.ok(bookService.findByParams(BookSearch.builder()
                        .title(title).genre(genre)
                        .firstname(firstname)
                        .lastname(lastname)
                        .build()));
    }
}
