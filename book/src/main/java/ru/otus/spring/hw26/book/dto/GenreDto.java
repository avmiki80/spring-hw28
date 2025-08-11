package ru.otus.spring.hw26.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id"})
public class GenreDto implements BaseDto{
    private Long id;
    private String title;
}
