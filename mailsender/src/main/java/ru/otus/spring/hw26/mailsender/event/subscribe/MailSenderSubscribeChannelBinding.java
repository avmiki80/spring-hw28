package ru.otus.spring.hw26.mailsender.event.subscribe;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(MailSendSubscribeChannel.class)
public class MailSenderSubscribeChannelBinding {
}
