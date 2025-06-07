package ru.otus.spring.hw26.book.repository.custom;

import org.springframework.context.annotation.Lazy;
import ru.otus.spring.hw26.book.domain.Genre;
import ru.otus.spring.hw26.book.repository.GenreRepository;
import ru.otus.spring.hw26.book.dto.GenreSearch;

public class GenreCustomRepositoryImpl implements GenreCustomRepository<Genre, GenreSearch> {
    private final GenreRepository genreRepository;

    public GenreCustomRepositoryImpl(@Lazy GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre findAndCreateIfAbsent(GenreSearch params) {
        return genreRepository.findByParams(params.getTitle())
                .stream()
                .findAny().orElseGet(() ->
                        genreRepository.save(new Genre(null, params.getTitle())));
    }
}
