package ru.otus.spring.h26.model.frommodarate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModerateResult {
    private Long commentId;
    private Boolean commentStatus;
    private String reason;
}
