package ru.otus.spring.hw26.loan.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.hw26.loan.event.mailsender.MailSenderChannelConstant;

import javax.annotation.PostConstruct;

@Configuration
public class BindingsConfiguration {

    private final String bindingGroup;
    private final BindingServiceProperties bindingServiceProperties;
    public BindingsConfiguration(
            @Value("${consumer.group}") String bindingGroup,
            BindingServiceProperties bindingServiceProperties) {
        this.bindingGroup = bindingGroup;
        this.bindingServiceProperties = bindingServiceProperties;
    }
    @PostConstruct
    void init(){
        bindingServiceProperties.getBindings()
                .put(MailSenderChannelConstant.MAIL_SEND_COMMAND,
                        createProducerProperties(MailSenderChannelConstant.MAIL_SEND_COMMAND));
    }


    private BindingProperties createConsumerProperties(String destination) {
        BindingProperties properties = new BindingProperties();
        properties.setDestination(destination);
        properties.setGroup(bindingGroup);
        return properties;
    }
    private BindingProperties createProducerProperties(String destination) {
        BindingProperties properties = new BindingProperties();
        properties.setDestination(destination);
        return properties;
    }
}
