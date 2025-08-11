package ru.otus.spring.hw26.book.event.moderate.publish;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import ru.otus.spring.h26.model.tomoderate.ModerateSearch;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.event.moderate.ModerateChannelConstant;

import java.util.List;

@MessagingGateway
public interface ModeratorGateway {
    @Gateway(
            requestChannel = ModerateChannelConstant.TO_MODERATE_COMMAND,
            headers = {
                    @GatewayHeader(name = "commandName", value = "moderateCommentCommand")

            })
    void sendCommentToModerate(Message<CommentDto> msg);

    @Gateway(
            requestChannel = ModerateChannelConstant.TO_MODERATE_COMMAND,
            headers = {
                    @GatewayHeader(name = "commandName", value = "moderateMassCommentCommand")

            })
    void sendMassCommentToModerate(Message<List<CommentDto>> msg);
    @Gateway(
            requestChannel = ModerateChannelConstant.TO_MODERATE_COMMAND,
            headers = {
                    @GatewayHeader(name = "commandName", value = "findNotModerateCommand")

            })
    void sendFindNotModerate(Message<ModerateSearch> msg);
}
