package ru.otus.spring.h26.model.tomoderate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @JsonProperty("id")
    private Long commentId;
    @JsonProperty("text")
    private String commentText;
}
