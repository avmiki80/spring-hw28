package ru.otus.spring.hw26.book.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class AuthorSearch {
    private String firstname;
    private String lastname;
}
