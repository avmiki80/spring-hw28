package ru.otus.spring.hw26.book.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.dto.CommentSearch;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentUserService implements CrudService<CommentDto, CommentSearch>{
    private final CrudService<CommentDto, CommentSearch> moderateCommentService;
    private final CustomSecurityContextService customSecurityContextService;
    @Override
    public CommentDto save(CommentDto obj) {
        obj.setUserId(customSecurityContextService.getUserId());
        return moderateCommentService.save(obj);
    }

    @Override
    public CommentDto update(long id, CommentDto obj) {
        return moderateCommentService.update(id, obj);
    }

    @Override
    public void deleteById(long id) {
        moderateCommentService.deleteById(id);
    }

    @Override
    public List<CommentDto> findAll() {
        return moderateCommentService.findAll();
    }

    @Override
    public CommentDto findById(long id) {
        return moderateCommentService.findById(id);
    }

    @Override
    public List<CommentDto> findByParams(CommentSearch params) {
        return moderateCommentService.findByParams(params);
    }

//    @Override
//    public CommentDto addUserId(CommentDto obj) {
//        obj.setUserId(getUserId());
//        return obj;
//    }
//
//    @Override
//    public List<CommentDto> addUserId(List<CommentDto> objs) {
//        final String userId = getUserId();
//        return objs.stream().parallel().peek(c -> c.setUserId(userId)).collect(Collectors.toList());
//    }
//
//    private String getUserId(){
//        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return jwt.getClaim("sub");
//    }
}
