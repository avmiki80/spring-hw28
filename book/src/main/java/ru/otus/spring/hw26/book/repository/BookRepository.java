package ru.otus.spring.hw26.book.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw26.book.domain.Book;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = {"genre", "author"})
    List<Book> findAll();

    @EntityGraph(attributePaths = {"genre", "author"})
    Optional<Book> findById(long id);
    @Query("select b from Book b left join fetch b.genre left join fetch b.author where " +
            "(:title is null or :title = '' or lower(b.title) like lower(concat(:title, '%'))) and " +
            "(:genre is null or :genre = '' or lower(b.genre.title) like lower(concat(:genre, '%'))) and " +
            "(:firstname is null or :firstname = '' or lower(b.author.firstname) like lower(concat(:firstname, '%'))) and " +
            "(:lastname is null or :lastname = '' or lower(b.author.lastname) like lower(concat(:lastname, '%')))")
    List<Book> findByParams(
            @Param("title") String title,
            @Param("genre") String genre,
            @Param("firstname") String firstname,
            @Param("lastname") String lastname
     );
    @Query("select count(b.id) from Book b")
    long count();
    @EntityGraph(attributePaths = {"genre", "author"})
    List<Book> findByTitleIn(Collection<String> titles);
    @Query("select b from Book b left join fetch b.author where :authorId is null or b.author.id = :authorId")
    List<Book> findByAuthorId(
            @Param("authorId") Long authorId);
    @Query("select b from Book b left join fetch b.genre where :genreId is null or b.genre.id = :genreId")
    List<Book> findByGenreId(
            @Param("genreId") Long genreId);
}
