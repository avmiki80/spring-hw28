package ru.otus.spring.hw26.book.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw26.book.domain.Audit;

@Repository
public interface AuditRepository extends CrudRepository<Audit, Long> {
}
