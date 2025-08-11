package ru.otus.spring.hw26.moderator.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.otus.spring.h26.model.mailsender.EmailDto;

@Component
@Slf4j
public class MailSenderFeignFallback implements MailSenderFeignClient {
    private final static String REASON = "MailSender service not available. Try again later.";
    @Override
    public ResponseEntity<Void> send(EmailDto emailDto) {
        log.debug(REASON);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
