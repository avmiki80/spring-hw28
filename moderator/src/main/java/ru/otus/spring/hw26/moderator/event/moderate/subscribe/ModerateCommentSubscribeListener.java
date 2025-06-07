package ru.otus.spring.hw26.moderator.event.moderate.subscribe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import ru.otus.spring.h26.model.tomoderate.Comment;
import ru.otus.spring.hw26.moderator.event.moderate.ModerateChannelConstant;
import ru.otus.spring.hw26.moderator.dto.ModerateSearch;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ModerateCommentSubscribeListener {
    private final ModerateSubscribeListenerService moderateSubscribeListenerService;
    private static final String MODERATE_COMMENT_STREAM_CONDITION =
            "headers['" + "commandName" + "'] == 'moderateCommentCommand'";
    @StreamListener(
            target = ModerateChannelConstant.TO_MODERATE_COMMAND,
            condition = MODERATE_COMMENT_STREAM_CONDITION)
    void moderateComment(
            Message<Comment> msg
    ){
        try {
            moderateSubscribeListenerService.moderateComment(msg);
        } catch (Exception e){
            log.info(e.getMessage());
        }
    }

    private static final String FIND_NOT_MODERATE_COMMENT_STREAM_CONDITION =
            "headers['" + "commandName" + "'] == 'findNotModerateCommand'";
    @StreamListener(
            target = ModerateChannelConstant.TO_MODERATE_COMMAND,
            condition = FIND_NOT_MODERATE_COMMENT_STREAM_CONDITION)
    void findNotModerateComment(
            Message<ModerateSearch> msg
    ){
        try {
            moderateSubscribeListenerService.findComment(msg);
        } catch (Exception e){
            log.info(e.getMessage());
        }
    }
}
