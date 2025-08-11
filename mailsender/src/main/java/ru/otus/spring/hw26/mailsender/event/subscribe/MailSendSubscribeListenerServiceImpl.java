package ru.otus.spring.hw26.mailsender.event.subscribe;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.mailsender.EmailDto;
import ru.otus.spring.hw26.mailsender.service.MailService;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailSendSubscribeListenerServiceImpl implements MailSendSubscribeListenerService {
    private final MailService mailService;

    @Override
    public void mailSend(Message<EmailDto> message) {
        try {
            log.debug("принятое сообщение {}", message);
            if (Objects.isNull(message.getPayload())) {
                log.info("Не передан payload {}", message);
//                throw new ServiceException("Не передан payload {}" + message);
            }
            EmailDto emailDto = message.getPayload();
            mailService.sendMessage(emailDto.getTo(), emailDto.getSubject(), emailDto.getText());

        } catch (Exception exception) {
            log.debug(exception.getMessage());
        }
    }
}
