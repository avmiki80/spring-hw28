package ru.otus.spring.h26.model.book;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class BookSearch {
    private String title;
    private String genre;
    private String firstname;
    private String lastname;
}
