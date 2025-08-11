package ru.otus.spring.hw26.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw26.book.domain.Genre;
import ru.otus.spring.hw26.book.repository.custom.GenreCustomRepository;
import ru.otus.spring.hw26.book.dto.GenreSearch;

import java.util.List;


@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>, GenreCustomRepository<Genre, GenreSearch> {
    @Query("select g from Genre g where :title is null or :title = '' or lower(g.title) like lower(concat(:title, '%'))")
    List<Genre> findByParams(@Param("title") String title);

    @Query("select count(g.id) from Genre g")
    long count();
}
