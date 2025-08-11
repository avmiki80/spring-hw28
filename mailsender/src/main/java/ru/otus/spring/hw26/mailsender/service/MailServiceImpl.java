package ru.otus.spring.hw26.mailsender.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw26.mailsender.exception.ServiceException;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService{
    private final JavaMailSender mailSender;
    @SneakyThrows
    @Override
    public void sendMessage(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        try {
            mailSender.send(message);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
}
