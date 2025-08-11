package ru.otus.spring.hw26.moderator.event.moderate.publish;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(ModeratorPublishChannel.class)
public class ModeratorPublishChannelBinding {
}
