package ru.otus.spring.hw26.mailsender.event.subscribe;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import ru.otus.spring.hw26.mailsender.event.MailSenderChannelConstant;

public interface MailSendSubscribeChannel {
    @Input(MailSenderChannelConstant.MAIL_SEND_COMMAND)
    SubscribableChannel mailSendCommand();
}
