package ru.otus.spring.hw26.moderator.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw26.moderator.domain.Moderate;

import java.time.LocalDateTime;

@Repository
public interface ModerateRepository extends JpaRepository<Moderate, Long> {
    @Query("select m from Moderate m where " +
            "(COALESCE(:text, '') = '' or lower(m.text) like lower(concat(:text, '%'))) and " +
            "(:frm is null or m.moderationTime >= :frm) and " +
            "(:to is null or m.moderationTime <= :to)")
    Page<Moderate> findByParams(
            @Param("text") String text,
            @Param("frm") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );
}
