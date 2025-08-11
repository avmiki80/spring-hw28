package ru.otus.spring.hw26.moderator.event.mailsender.publish;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(MailSenderPublishChannel.class)
public class MailSenderPublishChannelBinding {
}
