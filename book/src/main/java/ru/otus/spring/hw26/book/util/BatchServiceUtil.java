package ru.otus.spring.hw26.book.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BatchServiceUtil {
    public static <T> Collection<List<T>> splitToBatches(Collection<T> collection, int batchSize) {
        return IntStream.range(0, (collection.size() + batchSize - 1) / batchSize)
                .mapToObj(i -> collection.stream()
                        .skip(i * batchSize)
                        .limit(batchSize)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
