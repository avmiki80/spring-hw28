package ru.otus.spring.hw26.loan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw26.loan.dto.LoanDto;
import ru.otus.spring.hw26.loan.dto.LoanSearch;


import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckUserLoanService implements CrudService<LoanDto, LoanSearch> {
    private final CrudService<LoanDto, LoanSearch> loanService;
    private final CheckUser<LoanDto> checkUserLoan;
    @Override
    @Transactional
    public LoanDto save(LoanDto obj) {
        return loanService.save(obj);
    }

    @Override
    @Transactional
    public LoanDto update(long id, LoanDto obj) {
        LoanDto result = loanService.update(id, obj);
        checkUserLoan.checkUserId(result.getUserId());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        findById(id);
        loanService.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanDto> findAll() {
        throw new UnsupportedOperationException("method not allowed");
    }

    @Override
    @Transactional(readOnly = true)
    public LoanDto findById(long id) {
        LoanDto result = loanService.findById(id);
        checkUserLoan.checkUserId(result.getUserId());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanDto> findByParams(LoanSearch params) {
        Page<LoanDto> result = loanService.findByParams(params);
        result.forEach(i -> checkUserLoan.checkUserId(i.getUserId()));
        return result;
    }
}
