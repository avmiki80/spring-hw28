package ru.otus.spring.hw26.book.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.hw26.book.dto.CommentDto;

@Component
public class ModerateFeignFallback implements ModerateFeignClient {
    private final static String REASON = "Moderate service not available. Try again later.";
    @Override
    public ResponseEntity<ModerateResult> moderate(CommentDto comment) {
        return ResponseEntity.ok(new ModerateResult(comment.getId(), false, REASON));
    }
}
