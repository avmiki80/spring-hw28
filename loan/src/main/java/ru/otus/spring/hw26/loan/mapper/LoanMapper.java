package ru.otus.spring.hw26.loan.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.hw26.loan.domain.LoanBook;
import ru.otus.spring.hw26.loan.dto.LoanDto;
@Component
public class LoanMapper implements Mapper<LoanDto, LoanBook>{
    @Override
    public LoanDto toDto(LoanBook entity) {
        LoanDto loanDto = new LoanDto();
        loanDto.setId(entity.getId());
        loanDto.setLoanDate(entity.getLoanDate());
        loanDto.setReturnDate(entity.getReturnDate());
        loanDto.setDescription(entity.getDescription());
        loanDto.setBookId(entity.getBookId());
        loanDto.setFirstname(entity.getFirstname());
        loanDto.setLastname(entity.getLastname());
        loanDto.setEmail(entity.getEmail());
        loanDto.setUserId(entity.getUserId());
        return loanDto;
    }

    @Override
    public LoanBook toEntity(LoanDto dto) {
        LoanBook loanBook = new LoanBook();
        loanBook.setId(dto.getId());
        loanBook.setLoanDate(dto.getLoanDate());
        loanBook.setFirstname(dto.getFirstname());
        loanBook.setLastname(dto.getLastname());
        loanBook.setEmail(dto.getEmail());
        loanBook.setBookId(dto.getBookId());
        loanBook.setReturnDate(dto.getReturnDate());
        loanBook.setDescription(dto.getDescription());
        return loanBook;
    }
}
