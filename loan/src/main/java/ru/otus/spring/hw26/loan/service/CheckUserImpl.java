package ru.otus.spring.hw26.loan.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw26.loan.dto.LoanDto;
import ru.otus.spring.hw26.loan.exception.ServiceException;
import ru.otus.spring.hw26.loan.service.secure.CustomSecurityContextService;

@Service
@RequiredArgsConstructor
public class CheckUserImpl implements CheckUser<LoanDto>{
    private final CustomSecurityContextService customSecurityContextService;

    @Override
    public void checkUserId(String userId) {
        if(!userId.equals(customSecurityContextService.getUserId()))
            throw new ServiceException("not corrected userId");
    }
}
