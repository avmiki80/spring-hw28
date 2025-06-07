package ru.otus.spring.hw26.book.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.exception.ServiceException;
import ru.otus.spring.hw26.book.feign.ModerateFeignClient;

@Service
@Slf4j
@ConditionalOnProperty(name = "moderate", havingValue = "feign")
public class ModeratorServiceFeign implements ModeratorService {
    private final ModerateFeignClient moderateFeignClient;
    @Autowired
    public ModeratorServiceFeign(
            @Qualifier("ru.otus.spring.hw26.book.feign.ModerateFeignClient") ModerateFeignClient moderateFeignClient) {
        this.moderateFeignClient = moderateFeignClient;
    }

    @Override
    public ModerateResult toModerate(CommentDto commentDto) {
        log.debug("Sending comment for moderation: {}", commentDto);
        try {
            ResponseEntity<ModerateResult> response = moderateFeignClient.moderate(commentDto);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ServiceException("Moderation service returned status: " + response.getStatusCode());
            }
            return response.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Failed to moderate comment: " +  e.getMessage());
        }
    }
}
