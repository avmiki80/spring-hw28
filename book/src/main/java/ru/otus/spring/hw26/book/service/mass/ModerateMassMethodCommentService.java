package ru.otus.spring.hw26.book.service.mass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.frommodarate.ModerateResult;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.dto.CommentSearch;
import ru.otus.spring.hw26.book.service.CrudService;
import ru.otus.spring.hw26.book.service.ModeratorService;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ModerateMassMethodCommentService implements MassMethodService<CommentDto> {
    private final ModeratorService moderatorService;
    private final MassMethodService<CommentDto> massMethodComment;
    private final CrudService<CommentDto, CommentSearch> commentService;
    @Override
    public List<CommentDto> massCreate(List<CommentDto> objs) {
        List<CommentDto> result = massMethodComment.massCreate(objs);
        return sendCommentToModerate(result);
    }

    @Override
    public List<CommentDto> massUpdate(List<CommentDto> objs) {
        List<CommentDto> result = massMethodComment.massUpdate(objs);
        return sendCommentToModerate(result);
    }

    @Override
    public void massDelete(List<Long> ids) {
        massMethodComment.massDelete(ids);
    }

    private List<CommentDto> sendCommentToModerate(List<CommentDto> result) {
        result.forEach(c -> {
            ModerateResult moderateResult = moderatorService.toModerate(c);
            if(!moderateResult.getCommentStatus()) {
                c.setText(moderateResult.getReason());
                commentService.update(c.getId(), c);
            }
        });
        return result;
    }
}
