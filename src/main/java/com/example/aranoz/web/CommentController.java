package com.example.aranoz.web;

import com.example.aranoz.domain.dtoS.model.Comment.CommentFormDto;
import com.example.aranoz.services.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/Comment")
public class CommentController extends BaseController{
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/")
    public ModelAndView postCreateComment(CommentFormDto model){
        this.commentService.createComment(model);
        return super.redirect("/Product/details/"+ model.getProductId());
    }
}
