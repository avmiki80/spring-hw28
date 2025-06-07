package ru.otus.spring.hw26.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw26.book.dto.AuthorDto;
import ru.otus.spring.hw26.book.dto.AuthorSearch;
import ru.otus.spring.hw26.book.service.CrudService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {

    private final CrudService<AuthorDto, AuthorSearch> authorService;

    @PostMapping()
    public ResponseEntity<AuthorDto> save(@RequestBody AuthorDto authorDto){
        return ResponseEntity.ok(authorService.save(authorDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> update(@PathVariable(name = "id") Long id, @RequestBody AuthorDto authorDto){
        return ResponseEntity.ok(authorService.update(id, authorDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        authorService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> findById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(authorService.findById(id));
    }

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
