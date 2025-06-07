package ru.otus.spring.hw26.book.event.moderate.publish;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import ru.otus.spring.hw26.book.event.moderate.ModerateChannelConstant;

public interface ModeratorPublishChannel {
    @Output(ModerateChannelConstant.TO_MODERATE_COMMAND)
    MessageChannel moderateComment();
}
