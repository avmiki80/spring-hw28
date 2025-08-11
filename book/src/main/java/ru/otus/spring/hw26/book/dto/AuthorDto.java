package ru.otus.spring.hw26.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id"})
public class AuthorDto implements BaseDto{
    private Long id;

    private String firstname;

    private String lastname;
}
