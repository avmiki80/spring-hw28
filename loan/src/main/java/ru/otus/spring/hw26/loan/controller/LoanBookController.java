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
@RequestMapping("/loan")
@RequiredArgsConstructor
@Tag(name = "Loan API", description = "API для управления арендой книг")
public class LoanBookController {
    private final CrudService<LoanDto, LoanSearch> checkUserLoanService;
    private final CustomSecurityContextService customSecurityContextService;
    @Operation(summary = "Взять книгу в аренду", description = "Создает запись об аренде книги для авторизованного пользхователя")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping()
    public ResponseEntity<LoanDto> save(@RequestBody LoanDto bookDto) {
        return ResponseEntity.ok(checkUserLoanService.save(bookDto));
    }
    @Operation(summary = "Изменить данные об аренде", description = "Изменяет запись об аренде книги для авторизованного пользхователя")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<LoanDto> update(@PathVariable(name = "id") Long id, @RequestBody LoanDto bookDto) {
        return ResponseEntity.ok(checkUserLoanService.update(id, bookDto));
    }
    @Operation(summary = "Удалить данные об аренде", description = "Удаляет запись об аренде книги для авторизованного пользхователя")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        checkUserLoanService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Получить данные об аренде по Id", description = "Получить запись об аренде книги по Id для авторизованного пользхователя")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<LoanDto> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(checkUserLoanService.findById(id));
    }
    @Operation(summary = "Получить данные об аренде", description = "Получить запись об аренде книг для авторизованного пользхователя")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping()
    public ResponseEntity<Page<LoanDto>> find(
            @RequestParam(name = "loanDate", required = false) String loanDate,
            @RequestParam(name = "returnDate", required = false) String returnDate,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "dir", required = false) String dir
    ) {
        return ResponseEntity.ok(checkUserLoanService.findByParams(
                LoanSearch.builder()
                        .userid(customSecurityContextService.getUserId())
                        .loanDate(loanDate != null ? LocalDate.parse(loanDate) : null)
                        .returnDate(returnDate != null ? LocalDate.parse(returnDate) : null)
                        .pageNumber(page)
                        .pageSize(size)
                        .sortColumn(sort)
                        .sortDirection(dir)
                        .build()));
    }

}
