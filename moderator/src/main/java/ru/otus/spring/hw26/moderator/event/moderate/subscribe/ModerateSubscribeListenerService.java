package ru.otus.spring.hw26.moderator.event.moderate.subscribe;

import org.springframework.messaging.Message;
import ru.otus.spring.h26.model.tomoderate.Comment;
import ru.otus.spring.hw26.moderator.dto.ModerateSearch;

import java.util.List;

public interface ModerateSubscribeListenerService {
    void moderateComment(Message<Comment> message);
    void massModerateComment(Message<List<Comment>> message);

    void findComment(Message<ModerateSearch> message);
}
