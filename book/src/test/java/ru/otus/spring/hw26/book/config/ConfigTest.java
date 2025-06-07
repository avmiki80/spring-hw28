package ru.otus.spring.hw26.book.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.otus.spring.hw26.book.domain.Author;
import ru.otus.spring.hw26.book.domain.Book;
import ru.otus.spring.hw26.book.domain.Comment;
import ru.otus.spring.hw26.book.domain.Genre;
import ru.otus.spring.hw26.book.dto.AuthorDto;
import ru.otus.spring.hw26.book.dto.BookDto;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.dto.GenreDto;
import ru.otus.spring.hw26.book.mapper.*;
import ru.otus.spring.hw26.book.repository.AuthorRepository;
import ru.otus.spring.hw26.book.repository.BookRepository;
import ru.otus.spring.hw26.book.repository.CommentRepository;
import ru.otus.spring.hw26.book.repository.GenreRepository;
import ru.otus.spring.hw26.book.dto.AuthorSearch;
import ru.otus.spring.hw26.book.dto.BookSearch;
import ru.otus.spring.hw26.book.dto.CommentSearch;
import ru.otus.spring.hw26.book.dto.GenreSearch;
import ru.otus.spring.hw26.book.service.*;

@TestConfiguration
public class ConfigTest {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
    @Bean
    public Mapper<GenreDto, Genre> genreMapper(){
        return new GenreMapper(objectMapper());
    }
    @Bean
    public CrudService<GenreDto, GenreSearch> genreService(){
        return new GenreService(genreRepository, genreMapper());
    }
    @Bean
    public Mapper<AuthorDto, Author> authorMapper(){
        return new AuthorMapper(objectMapper());
    }
    @Bean
    public CrudService<AuthorDto, AuthorSearch> authorService(){
        return new AuthorService(authorRepository, authorMapper());
    }
    @Bean
    public Mapper<BookDto, Book> bookMapper(){
        return new BookMapper();
    }

    @Bean
    public CrudService<BookDto, BookSearch> bookService(){
        return new BookService(bookRepository, genreRepository, authorRepository, bookMapper());
    }
    @Bean
    public Mapper<CommentDto, Comment> commentMapper(){
        return new CommentMapper();
    }
    @Bean
    public CrudService<CommentDto, CommentSearch> commentService(){
        return new CommentService(commentRepository, bookRepository, commentMapper());
    }
}
