package ru.otus.spring.hw26.moderator.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.otus.spring.hw26.moderator.dto.BaseSearch;

import java.util.Objects;

public class PageRequestUtil {
    public static final String ID = "id";
    public static final String DESC = "desc";
    public static final String ASC = "asc";
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final int DEFAULT_PAGE_NUMBER = 0;

    public static<S extends BaseSearch> PageRequest createPageRequest(S search){
        if(Objects.isNull(search.getPageNumber()) || search.getPageNumber() < 0)
            search.setPageNumber(DEFAULT_PAGE_NUMBER);
        if(Objects.isNull(search.getPageSize()) || search.getPageSize() < 1)
            search.setPageSize(DEFAULT_PAGE_SIZE);
        return PageRequest.of(search.getPageNumber(), search.getPageSize(), createSort(search.getSortColumn(), search.getSortDirection()));
    }

    public static Sort createSort(String column, String direction) {
        if (isEmptyString(column))
            column = ID;
        if (isEmptyString(direction))
            direction = ASC;
        if (direction.equalsIgnoreCase(DESC))
            return Sort.by(Sort.Direction.DESC, column);
        return Sort.by(Sort.Direction.ASC, column);
    }
    public static boolean isEmptyString(String str){
        return Objects.isNull(str) || str.trim().isEmpty();
    }
}
