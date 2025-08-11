package ru.otus.spring.hw26.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.h26.model.book.BookDto;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDto {
    private Long id;
    private LocalDate loanDate;
    private LocalDate returnDate;

    private Long bookId;
    private BookDto bookDto;
//    private String bootkTtle;
//    private String bookGenre;
//    private String bookAuthorFirstname;
//    private String bookAuthorLastname;

    private String userId;
    private String firstname;
    private String lastname;
    private String email;
    private String description;
}
