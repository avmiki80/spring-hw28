package ru.otus.spring.hw26.loan.service.mail;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otus.spring.h26.model.mailsender.EmailDto;
import ru.otus.spring.hw26.loan.exception.ServiceException;


@Service
@Slf4j
//@ConditionalOnProperty(name="mailsender", matchIfMissing = true, havingValue = "rest-template")
@ConditionalOnProperty(name="mailsend", havingValue = "rest-template")
public class MailSenderServiceRest implements MailSenderService {
    private final RestTemplate restTemplate;
    private final String url;

    public MailSenderServiceRest(
            @Value("${mailsender-url}")String url,
            RestTemplate restTemplate
    ) {
        this.url = url;
        log.debug("MailSender service: {}", url);
        this.restTemplate = restTemplate;
    }


    @Override
    public void sendMail(EmailDto emailDto) {
        log.debug("Sending mail : {}", emailDto);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EmailDto> requestEntity = new HttpEntity<>(emailDto, headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    url + "/send",
                    HttpMethod.POST,
                    requestEntity,
                    Void.class
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ServiceException("MailSender service returned status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new ServiceException("Failed send : " +  e.getMessage());
        }
    }
}
