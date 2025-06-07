package ru.otus.spring.hw26.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw26.book.dto.GenreDto;
import ru.otus.spring.hw26.book.dto.GenreSearch;
import ru.otus.spring.hw26.book.service.CrudService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genre")
public class GenreController {
    private final CrudService<GenreDto, GenreSearch> genreService;

    @PostMapping()
    public ResponseEntity<GenreDto> save(@RequestBody GenreDto genreDto){
        return ResponseEntity.ok(genreService.save(genreDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> update(@PathVariable(name = "id") Long id, @RequestBody GenreDto genreDto){
        return ResponseEntity.ok(genreService.update(id, genreDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        genreService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> findById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(genreService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<GenreDto>> find(
            @RequestParam(name = "title", required = false) String title
    ){
        return ResponseEntity.ok(genreService.findByParams(GenreSearch.builder()
                .title(title)
                .build()));
    }
}
