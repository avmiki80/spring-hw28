package ru.otus.spring.hw26.book.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw26.book.domain.Comment;

import java.util.List;
import java.util.Optional;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"book"})
    List<Comment> findAll();
    @EntityGraph(attributePaths = {"book"})
    Optional<Comment> findById(long id);
    @Query("select c from Comment c left join fetch c.book where " +
            "(:bookIds is null or c.book.id in(:bookIds)) and " +
            "(:text is null or :text = '' or lower(c.text) like lower(concat('%', :text, '%'))) and " +
            "(:bookTitle is null or :bookTitle = '' or lower(c.book.title) like lower(concat(:bookTitle, '%')))")
    List<Comment> findByParams(@Param("text") String text, @Param("bookTitle") String bookTitle, @Param("bookIds") Iterable<Long> bookIdf);
    @Query("select count(c.id) from Comment c")
    long count();
}
