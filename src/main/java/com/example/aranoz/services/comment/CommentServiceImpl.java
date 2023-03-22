package com.example.aranoz.services.comment;

import com.example.aranoz.domain.dtoS.model.Comment.CommentFormDto;
import com.example.aranoz.domain.entities.Comment;
import com.example.aranoz.repository.CommentRepository;
import com.example.aranoz.services.product.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, ProductService productService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createComment(CommentFormDto model) {
        Comment comment = modelMapper.map(model, Comment.class);
        comment.setId(null);
        comment.setDate(new Date());
        comment.setProduct(productService.findById(model.getProductId()));

        this.commentRepository.save(comment);
    }

    @Override
    public List<Comment> getAllCommentsForProduct(String id) {
        return this.commentRepository.findAllByProductId(id);
    }
}
