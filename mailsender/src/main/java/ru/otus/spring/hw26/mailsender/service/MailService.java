package ru.otus.spring.hw26.mailsender.service;

public interface MailService {
    void sendMessage(String to, String subject, String text);
}
