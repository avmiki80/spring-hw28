package ru.otus.spring.hw26.moderator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.h26.model.tomoderate.Comment;
import ru.otus.spring.hw26.moderator.dto.CheckedCommentDto;
import ru.otus.spring.hw26.moderator.dto.ModerateDto;
import ru.otus.spring.hw26.moderator.dto.ModerateSearch;
import ru.otus.spring.hw26.moderator.service.security.CustomSecurityContextService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ModeratorServiceImpl implements ModeratorService{
    private final CrudService<ModerateDto, ModerateSearch> moderateService;
    private final CustomSecurityContextService securityContextService;
    private static final Integer THRESHOLD = 90;
    private static final String INFO_NOT_FOUND = "not found";

    public ModeratorServiceImpl(
            CrudService<ModerateDto, ModerateSearch> moderateService,
            CustomSecurityContextService securityContextService) {
        this.moderateService = moderateService;
        this.securityContextService = securityContextService;
    }

    @Override
    public CheckedCommentDto moderate(Comment comment) {
        CheckedCommentDto checkedComment = checkComment(comment);
        if(!checkedComment.getCommentStatus()){
            Map<String, String> userinfo = securityContextService.getUserInfo();
            moderateService.save(
                    ModerateDto.builder()
                            .commentId(checkedComment.getCommentId())
                            .text(checkedComment.getReason())
                            .userId(userinfo.getOrDefault("userId", INFO_NOT_FOUND))
                            .firstname(userinfo.getOrDefault("firstname", INFO_NOT_FOUND))
                            .lastname(userinfo.getOrDefault("lastname", INFO_NOT_FOUND))
                            .email(userinfo.getOrDefault("email", INFO_NOT_FOUND))
                            .build());
        }
        return checkedComment;
    }
    @Override
    public List<CheckedCommentDto> massModerate(List<Comment> comments) {
        List<CheckedCommentDto> checkedComments = comments.stream()
                .map(this::checkComment)
                .collect(Collectors.toList());
        Map<String, String> userinfo = securityContextService.getUserInfo();
        moderateService.saveAll(checkedComments.stream()
                .filter(i -> i.getCommentStatus().equals(false))
                .map(i -> ModerateDto.builder()
                        .commentId(i.getCommentId())
                        .text(i.getReason())
                        .userId(userinfo.getOrDefault("userId", INFO_NOT_FOUND))
                        .firstname(userinfo.getOrDefault("firstname", INFO_NOT_FOUND))
                        .lastname(userinfo.getOrDefault("lastname", INFO_NOT_FOUND))
                        .email(userinfo.getOrDefault("email", INFO_NOT_FOUND))
                        .build()
                )
                .collect(Collectors.toList()));

        return checkedComments;
    }

    private CheckedCommentDto checkComment(Comment comment){
        log.debug("Модерация коммента ID: {}, текст: {}", comment.getCommentId(), comment.getCommentText());
        if(ThreadLocalRandom.current().nextInt(0, 100) < THRESHOLD){
            return CheckedCommentDto.builder().commentId(comment.getCommentId()).commentStatus(true).build();
        } else {
            return CheckedCommentDto.builder()
                    .commentId(comment.getCommentId())
                    .reason("Not valid comment: " + comment.getCommentText())
                    .commentStatus(false)
                    .build();
        }
    }

}
