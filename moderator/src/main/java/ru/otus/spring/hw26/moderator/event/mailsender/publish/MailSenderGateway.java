package ru.otus.spring.hw26.moderator.event.mailsender.publish;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import ru.otus.spring.h26.model.mailsender.EmailDto;
import ru.otus.spring.hw26.moderator.event.mailsender.MailSenderChannelConstant;


@MessagingGateway
public interface MailSenderGateway {
    @Gateway(
            requestChannel = MailSenderChannelConstant.MAIL_SEND_COMMAND,
            headers = {
                    @GatewayHeader(name = "commandName", value = "mailSendCommand")

            })
    void sendMailToUser(Message<EmailDto> msg);

}
