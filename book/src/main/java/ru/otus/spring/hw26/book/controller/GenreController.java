package ru.otus.spring.hw26.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw26.book.dto.GenreDto;
import ru.otus.spring.hw26.book.dto.GenreSearch;
import ru.otus.spring.hw26.book.service.CrudService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genre")
@Tag(name = "Genre API", description = "API жанров")
public class GenreController {
    private final CrudService<GenreDto, GenreSearch> genreService;
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Добавить жанр", description = "Создает жанр")
    @PostMapping()
    public ResponseEntity<GenreDto> save(@RequestBody GenreDto genreDto){
        return ResponseEntity.ok(genreService.save(genreDto));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Изменить жанр", description = "Изменяет жанр")
    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> update(@PathVariable(name = "id") Long id, @RequestBody GenreDto genreDto){
        return ResponseEntity.ok(genreService.update(id, genreDto));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить жанр", description = "Удаляет жанр")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        genreService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Получить жанр по Id", description = "Возвращает жанр по Id")
    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> findById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(genreService.findById(id));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(summary = "Поиск жанров", description = "Возвращает коллекцию жанров")
    @GetMapping()
    public ResponseEntity<List<GenreDto>> find(
            @RequestParam(name = "title", required = false) String title
    ){
        return ResponseEntity.ok(genreService.findByParams(GenreSearch.builder()
                .title(title)
                .build()));
    }
}
