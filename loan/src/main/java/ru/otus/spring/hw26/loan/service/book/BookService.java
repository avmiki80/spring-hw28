package ru.otus.spring.hw26.loan.service.book;

import ru.otus.spring.h26.model.book.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(Long id);
    List<BookDto> findByParam(String title, String genre, String firstname, String lastname);
}
