package ru.otus.spring.hw26.book.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(of = {"id"})
public class BookDto implements BaseDto{
    private Long id;
    private String title;
    private String genre;
    private String firstname;
    private String lastname;
}
