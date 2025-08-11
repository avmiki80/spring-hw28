package ru.otus.spring.hw26.book.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.otus.spring.hw26.book.dto.BookDto;

@AllArgsConstructor
@NoArgsConstructor(staticName = "book")
@With
public class BookDTODataBuilder implements TestDataBuilder<BookDto> {
    private Long id = 1L;
    private String title = "Test1";
    private String genre = "Фантастика";
    private String firstname = "Александр";
    private String lastname = "Пушкин";
    @Override
    public BookDto build() {
        final BookDto book = new BookDto();
        book.setId(id);
        book.setTitle(title);
        book.setGenre(genre);
        book.setFirstname(firstname);
        book.setLastname(lastname);
        return book;
    }
}
