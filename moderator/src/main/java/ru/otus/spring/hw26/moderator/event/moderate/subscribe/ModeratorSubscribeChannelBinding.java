package ru.otus.spring.hw26.moderator.event.moderate.subscribe;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(ModerateSubscribeChannel.class)
public class ModeratorSubscribeChannelBinding {
}
