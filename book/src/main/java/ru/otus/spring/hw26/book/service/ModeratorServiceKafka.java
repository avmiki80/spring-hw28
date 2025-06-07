package ru.otus.spring.hw26.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.event.moderate.publish.ModeratorGateway;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "moderate", havingValue = "kafka")
public class ModeratorServiceKafka implements ModeratorService{
    private final ModeratorGateway moderatorGateway;
    @Override
    public ModerateResult toModerate(CommentDto commentDto) {
        Message<CommentDto> message = MessageBuilder.withPayload(commentDto)
                .setHeader("commandName", "moderateCommentCommand")
                .setHeader("messageGuid", UUID.randomUUID())
                .build();
        moderatorGateway.sendCommentToModerate(message);
        // Возвращаю результат со статусом true для поддержки пседосинхронности вызова.
        // Не придется ломать логику обработки если модерация коментария будет происходить синхронно, через REST
        return ModerateResult.builder().commentStatus(true).build();
    }
}
