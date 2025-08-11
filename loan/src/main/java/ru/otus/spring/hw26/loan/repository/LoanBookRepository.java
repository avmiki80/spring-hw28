package ru.otus.spring.hw26.loan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw26.loan.domain.LoanBook;

import java.time.LocalDate;

@Repository
public interface LoanBookRepository extends JpaRepository<LoanBook, Long> {
    @Query("select l from LoanBook l where " +
            "(:userId is null or l.userId = :userId) and " +
            "(cast(:loanDate as date) is null or l.loanDate >= :loanDate) and " +
            "(cast(:returnDate as date) is null or l.returnDate <=:returnDate) and " +
            "(:firstname is null or l.firstname = :firstname) and " +
            "(:lastname is null or l.lastname = :lastname) and " +
            "(:email is null or l.email = :email)"
    )
    Page<LoanBook> findByParams(
            @Param("userId") String userId,
            @Param("loanDate") LocalDate loanDate,
            @Param("returnDate") LocalDate returnDate,
            @Param("email") String email,
            @Param("firstname") String firstname,
            @Param("lastname") String lastname,
            Pageable pageable
    );
}
