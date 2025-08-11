package ru.otus.spring.hw26.book.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.hw26.book.domain.Author;
import ru.otus.spring.hw26.book.domain.Book;
import ru.otus.spring.hw26.book.domain.Genre;
import ru.otus.spring.hw26.book.dto.BookDto;

import java.util.Objects;

@Component
public class BookMapper implements Mapper<BookDto, Book> {
    @Override
    public BookDto toDto(Book entity) {
        return BookDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .genre(Objects.isNull(entity.getGenre()) || Objects.isNull(entity.getGenre().getTitle()) ? "" : entity.getGenre().getTitle())
                .firstname(Objects.isNull(entity.getAuthor()) || Objects.isNull(entity.getAuthor().getFirstname()) ? "" : entity.getAuthor().getFirstname())
                .lastname(Objects.isNull(entity.getAuthor()) || Objects.isNull(entity.getAuthor().getLastname()) ? "" : entity.getAuthor().getLastname())
                .build();
    }

    @Override
    public Book toEntity(BookDto dto) {
        return Book.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .genre(new Genre(null, dto.getGenre()))
                .author(new Author(null, dto.getFirstname(), dto.getLastname()))
                .build();
    }
}
