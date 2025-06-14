package ru.otus.spring.hw26.moderator.event.moderate.publish;

import org.springframework.data.domain.Page;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import ru.otus.spring.hw26.moderator.dto.CheckedCommentDto;
import ru.otus.spring.hw26.moderator.dto.ModerateDto;
import ru.otus.spring.hw26.moderator.event.moderate.ModerateChannelConstant;

import java.util.List;

@MessagingGateway
public interface ModeratorGateway {
    @Gateway(
            requestChannel = ModerateChannelConstant.FROM_MODERATE_COMMAND,
            headers = {
                    @GatewayHeader(name = "commandName", value = "fromModerateCommentCommand")

            })
    void sendCommentFromModerate(Message<CheckedCommentDto> msg);

    @Gateway(
            requestChannel = ModerateChannelConstant.FROM_MODERATE_COMMAND,
            headers = {
                    @GatewayHeader(name = "commandName", value = "fromMassModerateCommentCommand")

            })
    void sendMassCommentFromModerate(Message<List<CheckedCommentDto>> msg);
    @Gateway(
            requestChannel = ModerateChannelConstant.FROM_MODERATE_COMMAND,
            headers = {
                    @GatewayHeader(name = "commandName", value = "fromFindNotModerateCommand")

            })
    void sendNotModerateComment(Message<Page<ModerateDto>> msg);
}
