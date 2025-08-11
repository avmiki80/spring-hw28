package ru.otus.spring.hw26.loan.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.mailsender.EmailDto;
import ru.otus.spring.hw26.loan.domain.LoanBook;
import ru.otus.spring.hw26.loan.repository.LoanBookRepository;
import ru.otus.spring.hw26.loan.service.mail.MailSenderService;
import ru.otus.spring.hw26.loan.service.secure.KeycloakTokenService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckingReturnBookImpl implements CheckingReturnBook{
    private final LoanBookRepository loanBookRepository;
    private final MailSenderService mailSenderService;
    private final KeycloakTokenService keycloakTokenService;
    private final String jwkUri;

    public CheckingReturnBookImpl(
            LoanBookRepository loanBookRepository,
            MailSenderService mailSenderService,
            KeycloakTokenService keycloakTokenService,
            @Value("${jwk.uri}") String jwkUri) {
        this.loanBookRepository = loanBookRepository;
        this.mailSenderService = mailSenderService;
        this.keycloakTokenService = keycloakTokenService;
        this.jwkUri = jwkUri;
    }

    @Override
//    @Scheduled(cron = "0 0/30 * * * *")
    @Scheduled(cron = "0 0 0 * * ?")
    public void check() {
        int page = 0;
        int size = 100;
        boolean hasNext;
        String jwt = keycloakTokenService.getToken();
        SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthenticationToken(NimbusJwtDecoder.withJwkSetUri(jwkUri).build().decode(jwt)));
        List<EmailDto> allLoans = new ArrayList<>();
        do {
            Page<LoanBook> pageData = loanBookRepository.findByParams(
                    null,
                    null,
                    LocalDate.now(),
                    null,
                    null,
                    null,
                    PageRequest.of(page, size)
            );
            allLoans.addAll(pageData.getContent().stream().map(i -> new EmailDto(i.getEmail(), "Дата возврата", "Превышено дата возврата книги")).collect(Collectors.toList()));
            hasNext = pageData.hasNext();
            page++;
        } while (hasNext);
        allLoans.forEach(mailSenderService::sendMail);

    }
}
