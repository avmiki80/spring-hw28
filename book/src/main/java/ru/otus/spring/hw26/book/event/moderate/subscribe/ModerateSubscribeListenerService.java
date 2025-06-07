package ru.otus.spring.hw26.book.event.moderate.subscribe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.messaging.Message;
import ru.otus.spring.h26.model.frommodarate.Moderated;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.h26.model.frommodarate.PageDto;

public interface ModerateSubscribeListenerService {
    void fromModerateComment(Message<ModerateResult> message);

    void findNotModerateComment(Message<PageDto<Moderated>> message);
}
