package ru.otus.spring.hw26.moderator.event.mailsender;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MailSenderChannelConstant {
    public static final String MAIL_SEND_COMMAND = "mail-send";
}
