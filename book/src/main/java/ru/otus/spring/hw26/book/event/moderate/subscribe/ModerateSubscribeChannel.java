package ru.otus.spring.hw26.book.event.moderate.subscribe;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import ru.otus.spring.hw26.book.event.moderate.ModerateChannelConstant;

public interface ModerateSubscribeChannel {
    @Input(ModerateChannelConstant.FROM_MODERATE_COMMAND)
    SubscribableChannel moderateCommentCommand();
}
