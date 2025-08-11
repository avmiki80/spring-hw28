package ru.otus.spring.hw26.loan.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class LoanSearch extends BaseSearch{
    private String userid;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private String email;
    private String firstname;
    private String lastname;
}
