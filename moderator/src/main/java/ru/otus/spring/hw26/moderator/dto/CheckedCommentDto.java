package ru.otus.spring.hw26.moderator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckedCommentDto {
    private Long commentId;
    private Boolean commentStatus;
    private String reason;
}
