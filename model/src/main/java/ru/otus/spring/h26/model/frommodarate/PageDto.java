package ru.otus.spring.h26.model.frommodarate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class PageDto<T> {
    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
}
