package ru.otus.spring.h26.model.book;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(of = {"id"})
public class BookDto{
    private Long id;
    private String title;
    private String genre;
    private String firstname;
    private String lastname;
}
