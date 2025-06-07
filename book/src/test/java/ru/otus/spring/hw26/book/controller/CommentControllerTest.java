package ru.otus.spring.hw26.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.hw26.book.config.ControllerConfigTest;
import ru.otus.spring.hw26.book.data.CommentDtoDataBuilder;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.exception.ServiceException;
import ru.otus.spring.hw26.book.dto.CommentSearch;
import ru.otus.spring.hw26.book.service.CrudService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ControllerConfigTest.class)
@ExtendWith(SpringExtension.class)
class CommentControllerTest {
    private static String TEXT = "Some text";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CrudService<CommentDto, CommentSearch> commentService;

    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    @Test
    void shouldCorrectSaveNewComment() throws Exception {
        CommentDto comment = CommentDtoDataBuilder.comment().withId(null).build();
        String forSave = mapper.writeValueAsString(comment);

        CommentDto expectedComment = CommentDtoDataBuilder.comment().build();
        String expectedResult = mapper.writeValueAsString(expectedComment);

        given(commentService.save(comment)).willReturn(expectedComment);

        mvc.perform(post("/comment").contentType(APPLICATION_JSON)
                        .content(forSave))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    @Test
    void shouldCorrectUpdateCommentText() throws Exception {
        CommentDto comment = CommentDtoDataBuilder.comment().build();
        String forSave = mapper.writeValueAsString(comment);

        CommentDto expectedComment = CommentDtoDataBuilder.comment().withText("test").build();
        String expectedResult = mapper.writeValueAsString(expectedComment);

        given(commentService.update(comment.getId(), comment)).willReturn(expectedComment);

        mvc.perform(put("/comment/{id}", 1).contentType(APPLICATION_JSON).content(forSave))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    @Test
    void shouldReturnExpectedComments() throws Exception {
        List<CommentDto> comments = List.of(
                CommentDtoDataBuilder.comment().withId(1L).withText(TEXT).build()
        );

        List<CommentDto> expectedComments = List.of(
                CommentDtoDataBuilder.comment().withId(1L).withText(TEXT).build()
        );

        given(commentService.findByParams(CommentSearch.builder()
                .text(TEXT)
                .build())).willReturn(comments);

        mvc.perform(get("/comment").param("text", TEXT))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedComments)));

    }
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    @Test
    void shouldCorrectDeleteComment() throws Exception {
        mvc.perform(delete("/comment/1"))
                .andExpect(status().isNoContent());
        verify(commentService, times(1)).deleteById(1L);
    }
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    @Test
    void shouldReturnCorrectCommentByIdI() throws Exception {
        CommentDto expectedComment = CommentDtoDataBuilder.comment().withText(TEXT).build();
        given(commentService.findById(expectedComment.getId())).willReturn(expectedComment);

        mvc.perform(get("/comment/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedComment)));
    }
    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    @Test
    void shouldReturnExpectedErrorWhenCommentNotFound() throws Exception {
        given(commentService.findById(1L)).willThrow(new ServiceException("exception.object-not-found"));

        mvc.perform(get("/comment/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("exception.object-not-found"));
    }
}