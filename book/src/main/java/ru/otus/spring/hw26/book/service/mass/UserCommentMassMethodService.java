package ru.otus.spring.hw26.book.service.mass;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.service.CustomSecurityContextService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCommentMassMethodService implements MassMethodService<CommentDto>{
    private final MassMethodService<CommentDto> moderateCommentMassMethodService;
    private final CustomSecurityContextService customSecurityContextService;
    @Override
    public List<CommentDto> massCreate(List<CommentDto> objs) {
        String userId = customSecurityContextService.getUserId();
        objs = objs.stream().parallel().peek(c -> c.setUserId(userId)).collect(Collectors.toList());
        return moderateCommentMassMethodService.massCreate(objs);
    }

    @Override
    public List<CommentDto> massUpdate(List<CommentDto> objs) {
        return moderateCommentMassMethodService.massUpdate(objs);
    }

    @Override
    public void massDelete(List<Long> ids) {
        moderateCommentMassMethodService.massDelete(ids);
    }
}
