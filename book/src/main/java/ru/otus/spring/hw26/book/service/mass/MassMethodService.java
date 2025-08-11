package ru.otus.spring.hw26.book.service.mass;

import java.util.List;

public interface MassMethodService<D> {
    List<D> massCreate(List<D> objs);
    List<D> massUpdate(List<D> objs);
    void massDelete(List<Long> ids);
}
