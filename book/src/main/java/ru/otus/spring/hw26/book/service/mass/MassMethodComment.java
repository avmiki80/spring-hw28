package ru.otus.spring.hw26.book.service.mass;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw26.book.dto.CommentDto;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.spring.hw26.book.util.BatchServiceUtil.splitToBatches;

@Service
public class MassMethodComment implements MassMethodService<CommentDto> {
    private final BatchService<CommentDto> commentBatchService;
    private final int batchSize;

    public MassMethodComment(
            BatchService<CommentDto> commentBatchService,
            @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}") int batchSize
    ) {
        this.commentBatchService = commentBatchService;
        this.batchSize = batchSize;
    }

    @Override
    public List<CommentDto> massCreate(List<CommentDto> objs) {
        List<CommentDto> result = new ArrayList<>(objs.size());
        splitToBatches(objs, batchSize).forEach(b -> result.addAll(commentBatchService.batchSave(b)));
        return result;
    }

    @Override
    public List<CommentDto> massUpdate(List<CommentDto> objs) {
        List<CommentDto> result = new ArrayList<>(objs.size());
        splitToBatches(objs, batchSize).forEach(b -> result.addAll(commentBatchService.batchUpdate(b)));
        return result;
    }

    @Override
    public void massDelete(List<Long> ids) {
        splitToBatches(ids, batchSize).forEach(commentBatchService::batchDelete);
    }
}
