package ru.otus.spring.hw26.book.service.mass;

import java.util.List;

public interface BatchService<D> {
    List<D> batchSave(List<D> objs);
    List<D> batchUpdate(List<D> objs);
    void batchDelete(List<Long> ids);
}
