package ru.otus.spring.hw26.book.event.moderate.subscribe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.h26.model.frommodarate.Moderated;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.h26.model.frommodarate.PageDto;
import ru.otus.spring.hw26.book.domain.Comment;
import ru.otus.spring.hw26.book.repository.CommentRepository;
import ru.otus.spring.hw26.book.exception.ServiceException;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModerateSubscribeListenerServiceImpl implements ModerateSubscribeListenerService{
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void fromModerateComment(Message<ModerateResult> message) {
        try {
            if(Objects.isNull(message.getHeaders().get("commandName"))) {
                throw new ServiceException("Не передан payload {}" + message);
            }
            ModerateResult payload = message.getPayload();
            if(!payload.getCommentStatus()){
                Comment persistComment = commentRepository.findById(payload.getCommentId()).orElseThrow(() -> new ServiceException("exception.object-not-found"));
                persistComment.setText(payload.getReason());
            }
//            log.info("результат проверки коментария {}", payload);
        } catch (ServiceException exception){
            exception.getMessage();
        }
    }

    @Override
    @Transactional
    public void fromMassModerateComment(Message<List<ModerateResult>> message) {
        try {
            if(Objects.isNull(message.getHeaders().get("commandName"))) {
                throw new ServiceException("Не передан payload {}" + message);
            }
            List<ModerateResult> payload = message.getPayload();
            Map<Long, ModerateResult> rejectedCommentMap = payload.stream()
                    .filter(i -> !i.getCommentStatus())
                    .collect(Collectors.toMap(ModerateResult::getCommentId, Function.identity()));
            if(!rejectedCommentMap.isEmpty()){
                commentRepository.findAllById(rejectedCommentMap.keySet()).forEach(c -> c.setText(rejectedCommentMap.get(c.getId()).getReason()));
            }
//            log.debug("результат проверки коментария {}", payload);
        } catch (ServiceException exception){
            exception.getMessage();
        }
    }

    @Override
    public void findNotModerateComment(Message<PageDto<Moderated>> message) {
        try {
            if(Objects.isNull(message.getHeaders().get("commandName"))) {
                throw new ServiceException("Не передан payload {}" + message);
            }
            PageDto<Moderated> payload = message.getPayload();
            log.info("проверенные коментарии {}", payload);
        } catch (ServiceException exception){
            exception.getMessage();
        }
    }
}
