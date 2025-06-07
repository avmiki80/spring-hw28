package ru.otus.spring.hw26.book.event.moderate.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import ru.otus.spring.h26.model.frommodarate.Moderated;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.h26.model.frommodarate.PageDto;
import ru.otus.spring.hw26.book.event.moderate.ModerateChannelConstant;

@Configuration
@RequiredArgsConstructor
public class ModerateCommentSubscribeListener {
    private final ModerateSubscribeListenerService moderateSubscribeListenerService;
    private static final String MODERATE_COMMENT_STREAM_CONDITION =
            "headers['" + "commandName" + "'] == 'fromModerateCommentCommand'";
    @StreamListener(
            target = ModerateChannelConstant.FROM_MODERATE_COMMAND,
            condition = MODERATE_COMMENT_STREAM_CONDITION)
    void moderateComment(Message<ModerateResult> msg){
        moderateSubscribeListenerService.fromModerateComment(msg);
    }

    private static final String FIND_NOT_MODERATE_COMMENT_STREAM_CONDITION =
            "headers['" + "commandName" + "'] == 'fromFindNotModerateCommand'";
    @StreamListener(
            target = ModerateChannelConstant.FROM_MODERATE_COMMAND,
            condition = FIND_NOT_MODERATE_COMMENT_STREAM_CONDITION)
    void findNotModerateComment(Message<PageDto<Moderated>> msg){
        moderateSubscribeListenerService.findNotModerateComment(msg);
    }
}
