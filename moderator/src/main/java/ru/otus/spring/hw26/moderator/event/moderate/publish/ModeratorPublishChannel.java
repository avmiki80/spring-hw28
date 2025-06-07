package ru.otus.spring.hw26.moderator.event.moderate.publish;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import ru.otus.spring.hw26.moderator.event.moderate.ModerateChannelConstant;

public interface ModeratorPublishChannel {
    @Output(ModerateChannelConstant.FROM_MODERATE_COMMAND)
    MessageChannel fromModerateComment();
}
