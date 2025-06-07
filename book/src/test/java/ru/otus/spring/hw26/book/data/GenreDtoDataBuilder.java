package ru.otus.spring.hw26.book.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.otus.spring.hw26.book.dto.GenreDto;

@AllArgsConstructor
@NoArgsConstructor(staticName = "genre")
@With
public class GenreDtoDataBuilder implements TestDataBuilder<GenreDto> {
    private Long id = 1L;
    private String title = "title";
    @Override
    public GenreDto build() {
        final GenreDto genre = new GenreDto();
        genre.setId(id);
        genre.setTitle(title);
        return genre;
    }
}
