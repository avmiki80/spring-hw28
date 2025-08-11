package ru.otus.spring.h26.model.mailsender;

public interface EmailDtoProjection {
    String getTo();
    String getSubject();
    String getText();
}
