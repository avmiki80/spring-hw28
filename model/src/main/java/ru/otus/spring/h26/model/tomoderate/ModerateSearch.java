package ru.otus.spring.h26.model.tomoderate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter @Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ModerateSearch extends BaseSearch {
    private String text;
    private LocalDateTime from;
    private LocalDateTime to;

}
