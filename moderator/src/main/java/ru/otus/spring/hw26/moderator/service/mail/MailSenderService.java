package ru.otus.spring.hw26.moderator.service.mail;

import ru.otus.spring.h26.model.mailsender.EmailDto;

public interface MailSenderService {

    void sendMail(EmailDto emailDto);
}
