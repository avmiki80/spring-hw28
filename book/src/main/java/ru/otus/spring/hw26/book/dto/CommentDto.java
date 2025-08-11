package ru.otus.spring.hw26.book.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(of = {"id"})
public class CommentDto implements BaseDto{
    private Long id;
    private String text;
    private String bookTitle;
    private String userId;
}
