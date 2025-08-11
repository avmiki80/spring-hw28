package ru.otus.spring.hw26.moderator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.mailsender.EmailDto;
import ru.otus.spring.hw26.moderator.repository.ModerateRepository;
import ru.otus.spring.hw26.moderator.service.mail.MailSenderService;
import ru.otus.spring.hw26.moderator.service.security.KeycloakTokenService;

@Service
public class CheckViolatorImpl implements CheckViolator{
    private final KeycloakTokenService keycloakTokenService;
    private final MailSenderService mailSenderService;

    private final ModerateRepository moderateRepository;
    private final String jwkUri;

    public CheckViolatorImpl(
            KeycloakTokenService keycloakTokenService,
            MailSenderService mailSenderService,
            ModerateRepository moderateRepository,
            @Value("${jwk.uri}") String jwkUri
    ) {
        this.keycloakTokenService = keycloakTokenService;
        this.mailSenderService = mailSenderService;
        this.moderateRepository = moderateRepository;
        this.jwkUri = jwkUri;
    }

    @Override
    @Scheduled(cron = "0 0/30 * * * *")
//    @Scheduled(cron = "0 0 23 * * *")
    public void check() {
        String jwt = keycloakTokenService.getToken();
        SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthenticationToken(NimbusJwtDecoder.withJwkSetUri(jwkUri).build().decode(jwt)));
        moderateRepository.findViolator(5L)
                .forEach(i -> mailSenderService.sendMail(new EmailDto(i.getTo(), i.getSubject(), i.getText())));
    }
}
