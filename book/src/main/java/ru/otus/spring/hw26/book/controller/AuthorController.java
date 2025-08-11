package ru.otus.spring.hw26.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw26.book.dto.AuthorDto;
import ru.otus.spring.hw26.book.dto.AuthorSearch;
import ru.otus.spring.hw26.book.service.CrudService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/author")
@Tag(name = "Author API", description = "API авторов")
public class AuthorController {

    private final CrudService<AuthorDto, AuthorSearch> authorService;
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Добавить автора", description = "Создает автора")
    @PostMapping()
    public ResponseEntity<AuthorDto> save(@RequestBody AuthorDto authorDto){
        return ResponseEntity.ok(authorService.save(authorDto));
    }    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Изменить автора", description = "Изменяет автора")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> update(@PathVariable(name = "id") Long id, @RequestBody AuthorDto authorDto){
        return ResponseEntity.ok(authorService.update(id, authorDto));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить автора", description = "Удаляет автора")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        authorService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Получить автора по Id", description = "Возвращает автора по Id")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> findById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(authorService.findById(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Поиск авторов", description = "Возвращает коллекцию авторов")
    @GetMapping()
    public ResponseEntity<List<AuthorDto>> find(
            @RequestParam(name = "firstname", required = false) String firstname,
            @RequestParam(name = "lastname", required = false) String lastname
            ){
        return ResponseEntity.ok(authorService.findByParams(
                AuthorSearch.builder()
                .firstname(firstname)
                .lastname(lastname)
                .build()));
    }
}
