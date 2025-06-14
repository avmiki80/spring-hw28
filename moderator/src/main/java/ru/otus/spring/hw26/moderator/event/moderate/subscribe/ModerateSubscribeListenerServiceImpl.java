package ru.otus.spring.hw26.moderator.event.moderate.subscribe;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import ru.otus.spring.h26.model.tomoderate.Comment;
import ru.otus.spring.hw26.moderator.dto.CheckedCommentDto;
import ru.otus.spring.hw26.moderator.dto.ModerateDto;
import ru.otus.spring.hw26.moderator.event.moderate.publish.ModeratorGateway;
import ru.otus.spring.hw26.moderator.dto.ModerateSearch;
import ru.otus.spring.hw26.moderator.service.CrudService;
import ru.otus.spring.hw26.moderator.service.ModeratorService;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModerateSubscribeListenerServiceImpl implements ModerateSubscribeListenerService {
    private final ModeratorService moderatorService;
    private final ModeratorGateway moderatorGateway;
    private final CrudService<ModerateDto, ModerateSearch> crudService;
    private final ObjectMapper objectMapper;

    @Override
    public void moderateComment(Message<Comment> message) {
        try {
            log.debug("принятое сообщение {}", message);
            if (Objects.isNull(message.getPayload())) {
                log.info("Не передан payload {}", message);
//                throw new ServiceException("Не передан payload {}" + message);
            }
            Comment comment = message.getPayload();
            CheckedCommentDto checkedCommentDto = moderatorService.moderate(comment);
            log.debug("результат проверки коментария {}", checkedCommentDto);
            Message<CheckedCommentDto> msg = MessageBuilder.withPayload(checkedCommentDto)
                    .setHeader("messageGuid", message.getHeaders().get("messageGuid"))
//                    .setHeader("commandName", message.getHeaders().get("commandName"))
                    .build();
//            if (!checkedCommentDto.getCommentStatus())
            moderatorGateway.sendCommentFromModerate(msg);
        } catch (Exception exception) {
            log.debug(exception.getMessage());
        }
    }

    @Override
    public void massModerateComment(Message<List<Comment>> message) {
        try {
            log.debug("принятое сообщение {}", message);
            if (Objects.isNull(message.getPayload())) {
                log.info("Не передан payload {}", message);
            }
            List<Comment> comments = message.getPayload();
            if(!comments.isEmpty()){
                List<CheckedCommentDto> checkedComments = moderatorService.massModerate(comments);
                Message<List<CheckedCommentDto>> msg = MessageBuilder.withPayload(checkedComments)
                        .setHeader("messageGuid", message.getHeaders().get("messageGuid"))
                        .build();
                moderatorGateway.sendMassCommentFromModerate(msg);
            }

        } catch (Exception exception) {
            log.debug(exception.getMessage());
        }
    }

    @Override
    public void findComment(Message<ModerateSearch> message) {
        try {
            log.debug("принятое сообщение {}", message);
            Object payload = message.getPayload();
            if (payload == KafkaNull.INSTANCE || Objects.isNull(payload)) {
                log.debug("Не передан payload {}", message);
//                throw new ServiceException("Не передан payload {}" + message);
            }
            Page<ModerateDto> comments = crudService.findByParams(message.getPayload());
//            Page<ModerateDto> comments = crudService.findByParams(parsingQuery(message.getPayload()));
            log.debug("результат найденые коментарии {}", comments);
            Message<Page<ModerateDto>> result = MessageBuilder.withPayload(comments)
                    .setHeader("messageGuid", message.getHeaders().get("messageGuid"))
//                    .setHeader("commandName", message.getHeaders().get("commandName"))
                    .build();
            moderatorGateway.sendNotModerateComment(result);
        } catch (Exception exception) {
            log.debug(exception.getMessage());
        }
    }

    private ModerateSearch parsingQuery(String query) {
        if(query == null)
            return ModerateSearch.builder().build();
        Map<String, String> parametersMap = new HashMap<>();
        String[] queryPairs = query.split("&");

        for (String pair : queryPairs) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? pair.substring(0, idx) : pair;
            String value = idx > 0 && pair.length() > idx + 1 ? pair.substring(idx + 1) : null;
            key = needsEncoding(key) ? UriUtils.encode(key, StandardCharsets.UTF_8) : key;
            value = value != null && needsEncoding(value) ? UriUtils.encode(value, StandardCharsets.UTF_8) : value;
            parametersMap.put(key, value);
        }
        return objectMapper.convertValue(parametersMap, ModerateSearch.class);
    }

    private static boolean needsEncoding(String value) {
        return !value.matches("^[a-zA-Z0-9_.~%-]+$");
    }
}
