package ru.otus.spring.hw26.moderator.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.mailsender.EmailDto;
import ru.otus.spring.hw26.moderator.exception.ServiceException;
import ru.otus.spring.hw26.moderator.feign.MailSenderFeignClient;

@Service
@Slf4j
@ConditionalOnProperty(name = "mailsend", havingValue = "feign")
public class MailSenderServiceFeign implements MailSenderService{
    private final MailSenderFeignClient mailSenderFeignClient;
    @Autowired
    public MailSenderServiceFeign(
            @Qualifier("ru.otus.spring.hw26.moderator.feign.MailSenderFeignClient") MailSenderFeignClient mailSenderFeignClient) {
        this.mailSenderFeignClient = mailSenderFeignClient;
    }

    @Override
    public void sendMail(EmailDto emailDto) {
        log.debug("Sending email: {}", emailDto);
        try {
            ResponseEntity<Void> response = mailSenderFeignClient.send(emailDto);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ServiceException("MailSender service returned status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Failed send: " +  e.getMessage());
        }
    }
}
