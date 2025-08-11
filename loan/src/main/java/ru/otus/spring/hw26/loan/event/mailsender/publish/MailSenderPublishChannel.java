package ru.otus.spring.hw26.loan.event.mailsender.publish;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import ru.otus.spring.hw26.loan.event.mailsender.MailSenderChannelConstant;

public interface MailSenderPublishChannel {
    @Output(MailSenderChannelConstant.MAIL_SEND_COMMAND)
    MessageChannel mailSend();
}
