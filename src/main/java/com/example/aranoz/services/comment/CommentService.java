package com.example.aranoz.services.comment;

import com.example.aranoz.domain.dtoS.model.Comment.CommentFormDto;
import com.example.aranoz.domain.entities.Comment;

import java.util.List;

public interface CommentService {
    void createComment(CommentFormDto model);
    List<Comment> getAllCommentsForProduct(String id);
}
