package ru.otus.spring.h26.model.mailsender;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailDto{
    private String to;
    private String subject;
    private String text;
}
