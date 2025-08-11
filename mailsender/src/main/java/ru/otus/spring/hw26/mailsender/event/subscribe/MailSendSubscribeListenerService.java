package ru.otus.spring.hw26.mailsender.event.subscribe;

import org.springframework.messaging.Message;
import ru.otus.spring.h26.model.mailsender.EmailDto;

public interface MailSendSubscribeListenerService {
    void mailSend(Message<EmailDto> message);
}
