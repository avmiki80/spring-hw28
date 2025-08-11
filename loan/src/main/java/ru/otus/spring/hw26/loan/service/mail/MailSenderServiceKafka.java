package ru.otus.spring.hw26.loan.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.mailsender.EmailDto;
import ru.otus.spring.hw26.loan.event.mailsender.publish.MailSenderGateway;
import ru.otus.spring.hw26.loan.service.secure.CustomSecurityContextService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "mailsend", havingValue = "kafka")
public class MailSenderServiceKafka implements MailSenderService{
    private final MailSenderGateway mailSenderGateway;
    private final CustomSecurityContextService securityContextService;
    @Override
    public void sendMail(EmailDto emailDto) {
        Message<EmailDto> message = MessageBuilder.withPayload(emailDto)
                .setHeader("commandName", "mailSendCommand")
                .setHeader("messageGuid", UUID.randomUUID())
                .setHeader("Authorization", securityContextService.getJwtToken())
                .build();
        mailSenderGateway.sendMailToUser(message);
    }
}
