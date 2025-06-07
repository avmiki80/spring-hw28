package ru.otus.spring.hw26.moderator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.tomoderate.Comment;
import ru.otus.spring.hw26.moderator.dto.CheckedCommentDto;
import ru.otus.spring.hw26.moderator.dto.ModerateDto;
import ru.otus.spring.hw26.moderator.dto.ModerateSearch;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModeratorServiceImpl implements ModeratorService{
    private final CrudService<ModerateDto, ModerateSearch> moderateService;
    private static final Integer THRESHOLD = 90;
    @Override
    public CheckedCommentDto moderate(Comment comment) {
        log.debug(comment.getCommentText());
//        throw new ServiceException("some error");
        if(RandomUtils.nextInt(0, 100) < THRESHOLD){
            return CheckedCommentDto.builder().commentStatus(true).build();
        } else {
            moderateService.save(
                    ModerateDto.builder()
                            .commentId(comment.getCommentId())
                            .text(comment.getCommentText())
                            .build()
            );
            return CheckedCommentDto.builder().commentId(comment.getCommentId()).reason("Not valid comment: " + comment.getCommentText()).commentStatus(false).build();
        }
    }
}
