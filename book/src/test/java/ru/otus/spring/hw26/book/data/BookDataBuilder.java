package ru.otus.spring.hw26.book.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.otus.spring.hw26.book.domain.Author;
import ru.otus.spring.hw26.book.domain.Book;
import ru.otus.spring.hw26.book.domain.Genre;

@AllArgsConstructor
@NoArgsConstructor(staticName = "book")
@With
public class BookDataBuilder implements TestDataBuilder<Book> {
    private Long id = 1L;
    private String title = "Test1";
    private Genre genre = GenreDataBuilder.genre().withId(1L).withTitle("Фантастика").build();
    private Author author = AuthorDataBuilder.author().withId(1L).withFirstname("Александр").withLastname("Пушкин").build();

    @Override
    public Book build() {
        final Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return book;
    }
}
