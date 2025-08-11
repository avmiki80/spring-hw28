package ru.otus.spring.hw26.mailsender.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.h26.model.mailsender.EmailDto;
import ru.otus.spring.hw26.mailsender.service.MailService;

@RestController
@RequestMapping("/send")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    @PostMapping()
    public ResponseEntity<Void> send(@RequestBody EmailDto mailDto) {
        mailService.sendMessage(mailDto.getTo(), mailDto.getSubject(), mailDto.getText());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
