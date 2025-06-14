package ru.otus.spring.hw26.moderator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.tomoderate.Comment;
import ru.otus.spring.hw26.moderator.dto.CheckedCommentDto;
import ru.otus.spring.hw26.moderator.dto.ModerateDto;
import ru.otus.spring.hw26.moderator.dto.ModerateSearch;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ModeratorServiceImpl implements ModeratorService{
    private final CrudService<ModerateDto, ModerateSearch> moderateService;
    private static final Integer THRESHOLD = 90;

    public ModeratorServiceImpl(
            CrudService<ModerateDto, ModerateSearch> moderateService) {
        this.moderateService = moderateService;
    }

    @Override
    public CheckedCommentDto moderate(Comment comment) {
        CheckedCommentDto checkedComment = checkComment(comment);
        if(!checkedComment.getCommentStatus())
            moderateService.save(ModerateDto.builder().commentId(checkedComment.getCommentId()).text(checkedComment.getReason()).build());
        return checkedComment;
    }
    @Override
    public List<CheckedCommentDto> massModerate(List<Comment> comments) {
        List<CheckedCommentDto> checkedComments = comments.stream()
                .map(this::checkComment)
                .collect(Collectors.toList());

        moderateService.saveAll(checkedComments.stream()
                .filter(i -> i.getCommentStatus().equals(false))
                .map(i -> ModerateDto.builder().commentId(i.getCommentId()).text(i.getReason()).build())
                .collect(Collectors.toList()));

        return checkedComments;
    }

    private CheckedCommentDto checkComment(Comment comment){
        log.debug("Модерация коммента ID: {}, текст: {}", comment.getCommentId(), comment.getCommentText());
        if(ThreadLocalRandom.current().nextInt(0, 100) < THRESHOLD){
            return CheckedCommentDto.builder().commentId(comment.getCommentId()).commentStatus(true).build();
        } else {
            return CheckedCommentDto.builder().commentId(comment.getCommentId()).reason("Not valid comment: " + comment.getCommentText()).commentStatus(false).build();
        }
    }

    private List<Comment> convertList(List<?> rawList) {
        return rawList.stream()
                .map(item -> new Comment(
                        ((Integer)((LinkedHashMap<?, ?>)item).get("id")).longValue(),
                        (String)((LinkedHashMap<?, ?>)item).get("text")
                ))
                .collect(Collectors.toList());
    }
}
