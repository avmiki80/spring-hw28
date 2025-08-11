package ru.otus.spring.hw26.loan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw26.loan.dto.LoanDto;
import ru.otus.spring.hw26.loan.dto.LoanSearch;
import ru.otus.spring.hw26.loan.service.CrudService;
import ru.otus.spring.hw26.loan.service.secure.CustomSecurityContextService;

import java.time.LocalDate;

@RestController
@RequestMapping("/loan-admin")
@RequiredArgsConstructor
@Tag(name = "Loan Admin API", description = "API для администрирования управления арендой книг")
public class AdminLoanBookController {
    private final CrudService<LoanDto, LoanSearch> loanService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping()
    @Operation(summary = "Взять книгу в аренду", description = "Создает запись об аренде книги")
    public ResponseEntity<LoanDto> save(@RequestBody LoanDto bookDto) {
        return ResponseEntity.ok(loanService.save(bookDto));
    }
    @Operation(summary = "Изменить данные об аренде", description = "Изменяет запись об аренде книги")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LoanDto> update(@PathVariable(name = "id") Long id, @RequestBody LoanDto bookDto) {
        return ResponseEntity.ok(loanService.update(id, bookDto));
    }
    @Operation(summary = "Удалить данные об аренде", description = "Удаляет запись об аренде книги")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        loanService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Получить данные об аренде по Id", description = "Получить запись об аренде книги по Id")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(loanService.findById(id));
    }
    @Operation(summary = "Получить данные об аренде", description = "Получить запись об аренде книг")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<Page<LoanDto>> find(
            @RequestParam(name = "firstname", required = false) String firstname,
            @RequestParam(name = "lastname", required = false) String lastname,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "loanDate", required = false) String loanDate,
            @RequestParam(name = "returnDate", required = false) String returnDate,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "dir", required = false) String dir
    ) {
        return ResponseEntity.ok(loanService.findByParams(
                LoanSearch.builder()
                        .firstname(firstname)
                        .lastname(lastname)
                        .email(email)
                        .loanDate(loanDate != null ? LocalDate.parse(loanDate) : null)
                        .returnDate(returnDate != null ? LocalDate.parse(returnDate) : null)
                        .pageNumber(page)
                        .pageSize(size)
                        .sortColumn(sort)
                        .sortDirection(dir)
                        .build()));
    }

}
