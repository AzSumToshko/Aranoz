package com.example.aranoz.web;

import com.example.aranoz.domain.dtoS.model.Comment.CommentFormDto;
import com.example.aranoz.services.comment.CommentService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CommentController(commentService))
                .setControllerAdvice()
                .build();
    }

    @Test
    public void testPostCreateCommentSuccess() throws Exception {
        CommentFormDto commentFormDto = new CommentFormDto();
        commentFormDto.setProductId("test123");
        mockMvc.perform(post("/Comment/").flashAttr("model", commentFormDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/Product/details/test123"))
                .andExpect(model().hasNoErrors());
        verify(commentService, times(1)).createComment(commentFormDto);
    }

    @Test
    public void testPostCreateCommentValidationFail() throws Exception {
        CommentFormDto commentFormDto = new CommentFormDto();
        commentFormDto.setProductId("test123");
        commentFormDto.setMessage(""); // this will cause validation error as content is required
        mockMvc.perform(post("/Comment/").flashAttr("model", commentFormDto))
                .andExpect(status().isOk())
                .andExpect(view().name("comment/create"))
                .andExpect(model().attributeHasFieldErrors("model", "content"));
        verify(commentService, never()).createComment(commentFormDto);
    }
}