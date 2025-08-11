package ru.otus.spring.hw26.mailsender.event.subscribe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import ru.otus.spring.h26.model.mailsender.EmailDto;
import ru.otus.spring.hw26.mailsender.event.MailSenderChannelConstant;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MailSendSubscribeListener {
    private final MailSendSubscribeListenerService mailSendSubscribeListenerService;
    private static final String MAIL_SEND_STREAM_CONDITION =
            "headers['" + "commandName" + "'] == 'mailSendCommand'";
    @StreamListener(
            target = MailSenderChannelConstant.MAIL_SEND_COMMAND,
            condition = MAIL_SEND_STREAM_CONDITION)
    void mailSend(
            Message<EmailDto> msg
    ){
        try {
            mailSendSubscribeListenerService.mailSend(msg);
        } catch (Exception e){
            log.info(e.getMessage());
        }
    }
}
