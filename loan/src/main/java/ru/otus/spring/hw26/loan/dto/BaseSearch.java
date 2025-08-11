package ru.otus.spring.hw26.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
@Getter @Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseSearch implements Serializable {

    private Integer pageNumber;
    private Integer pageSize;
    private String sortColumn;
    private String sortDirection;
}
