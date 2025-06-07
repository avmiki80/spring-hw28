package ru.otus.spring.hw26.moderator.event.moderate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ModerateChannelConstant {
    public static final String TO_MODERATE_COMMAND = "to-moderate";
    public static final String FROM_MODERATE_COMMAND = "from-moderate";
}
