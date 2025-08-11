package ru.otus.spring.hw26.book.service.mass;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.service.ModeratorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.otus.spring.hw26.book.util.BatchServiceUtil.splitToBatches;

@Service
public class ModerateCommentMassMethodService implements MassMethodService<CommentDto> {
    private final ModeratorService moderatorService;
    private final MassMethodService<CommentDto> commentMassMethodService;
    private final int batchSize;

    public ModerateCommentMassMethodService(
            ModeratorService moderatorService,
            MassMethodService<CommentDto> commentMassMethodService,
            @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}") int batchSize
    ) {
        this.moderatorService = moderatorService;
        this.commentMassMethodService = commentMassMethodService;
        this.batchSize = batchSize;
    }

    @Override
    public List<CommentDto> massCreate(List<CommentDto> objs) {
//        List<CommentDto> result = massMethodComment.massCreate(objs);
        List<CommentDto> result = new ArrayList<>(objs.size());
        splitToBatches(commentMassMethodService.massCreate(objs), batchSize).forEach(c -> result.addAll(processComments(c)));
//        return processComments(result);
        return result;
    }

    @Override
    public List<CommentDto> massUpdate(List<CommentDto> objs) {
        List<CommentDto> result = new ArrayList<>(objs.size());
//        List<CommentDto> result = massMethodComment.massUpdate(objs);
        splitToBatches(commentMassMethodService.massUpdate(objs), batchSize).forEach(c -> result.addAll(processComments(c)));
        return processComments(result);
    }

    @Override
    public void massDelete(List<Long> ids) {
        commentMassMethodService.massDelete(ids);
    }

    private List<CommentDto> processComments(List<CommentDto> comments) {
        if (comments.isEmpty()) {
            return comments;
        }
        List<ModerateResult> rejectedComments = moderatorService.toModerate(comments).stream().filter(i -> !i.getCommentStatus()).collect(Collectors.toList());
        if(!rejectedComments.isEmpty()){
            Map<Long, CommentDto> commentMap = comments.stream().collect(Collectors.toMap(CommentDto::getId, Function.identity()));

            List<CommentDto> toUpdate = rejectedComments.stream()
                    .map(i -> {
                        CommentDto comment = commentMap.get(i.getCommentId());
                        comment.setText(i.getReason());
                        return comment;
                    }).collect(Collectors.toList());

            commentMassMethodService.massUpdate(toUpdate);
        }
        return comments;
    }
}
