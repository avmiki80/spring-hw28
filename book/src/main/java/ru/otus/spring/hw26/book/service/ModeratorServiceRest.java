package ru.otus.spring.hw26.book.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.exception.ServiceException;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
//@ConditionalOnMissingBean(ModeratorServiceKafka.class)
@ConditionalOnProperty(name="moderate", matchIfMissing = true, havingValue = "rest-template")
public class ModeratorServiceRest implements ModeratorService {
    private final RestTemplate restTemplate;
    private final String url;

    public ModeratorServiceRest(
            @Value("${moderator-url}")String url,
            RestTemplate restTemplate
    ) {
        this.url = url;
        log.debug("Moderator service: {}", url);
        this.restTemplate = restTemplate;
    }

    @Override
    public ModerateResult toModerate(CommentDto commentDto) {
        log.debug("Sending comment for moderation: {}", commentDto);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CommentDto> requestEntity = new HttpEntity<>(commentDto, headers);

            ResponseEntity<ModerateResult> response = restTemplate.exchange(
                    url + "/moderate",
                    HttpMethod.POST,
                    requestEntity,
                    ModerateResult.class
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ServiceException("Moderation service returned status: " + response.getStatusCode());
            }
            return response.getBody();
        } catch (Exception e) {
            throw new ServiceException("Failed to moderate comment: " +  e.getMessage());
        }
    }

    @Override
    public List<ModerateResult> toModerate(List<CommentDto> commentDtos) {
        log.debug("Sending comment for moderation: {}", commentDtos);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<CommentDto>> requestEntity = new HttpEntity<>(commentDtos, headers);

            ResponseEntity<List<ModerateResult>> response = restTemplate.exchange(
                    url + "/moderates",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {
                    }
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ServiceException("Moderation service returned status: " + response.getStatusCode());
            }
            return response.getBody();
        } catch (Exception e) {
            throw new ServiceException("Failed to moderate comment: " +  e.getMessage());
        }
    }
}
