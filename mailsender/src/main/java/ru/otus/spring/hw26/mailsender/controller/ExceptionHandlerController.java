package ru.otus.spring.hw26.mailsender.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.spring.hw26.mailsender.exception.ServiceException;


@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleServiceException(ServiceException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
