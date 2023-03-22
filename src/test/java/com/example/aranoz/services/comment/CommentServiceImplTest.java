package com.example.aranoz.services.comment;

import com.example.aranoz.domain.dtoS.model.Comment.CommentFormDto;
import com.example.aranoz.domain.entities.Comment;
import com.example.aranoz.domain.entities.Product;
import com.example.aranoz.repository.CommentRepository;
import com.example.aranoz.services.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {
    @Mock
    CommentRepository commentRepository;

    @Mock
    ProductService productService;

    ModelMapper modelMapper = new ModelMapper();

    CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        commentService = new CommentServiceImpl(commentRepository, productService, modelMapper);
    }

    @Test
    void createComment() {
        // given
        CommentFormDto commentFormDto = new CommentFormDto();
        commentFormDto.setProductId("product-123");
        commentFormDto.setName("John Doe");
        commentFormDto.setMessage("This is a test comment");

        Comment savedComment = new Comment();
        savedComment.setId("test-id6");
        savedComment.setName("John Doe");
        savedComment.setMessage("This is a test comment");
        savedComment.setDate(new Date());
        savedComment.setProduct(new Product("product-123"));

        when(productService.findById(commentFormDto.getProductId())).thenReturn(new Product("product-123"));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        // when
        commentService.createComment(commentFormDto);

        // then
        verify(productService).findById(commentFormDto.getProductId());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void getAllCommentsForProduct() {
        // given
        String productId = "product-123";
        Comment comment1 = new Comment();
        comment1.setId("comment-id1");
        comment1.setName("John Doe");
        comment1.setMessage("This is a test comment");
        comment1.setDate(new Date());
        comment1.setProduct(new Product("product-123"));

        Comment comment2 = new Comment();
        comment2.setId("comment-id2");
        comment2.setName("Jane Doe");
        comment2.setMessage("This is another test comment");
        comment2.setDate(new Date());
        comment2.setProduct(new Product("product-123"));

        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(comment1);
        expectedComments.add(comment2);

        when(commentRepository.findAllByProductId(productId)).thenReturn(expectedComments);

        // when
        List<Comment> actualComments = commentService.getAllCommentsForProduct(productId);

        // then
        verify(commentRepository).findAllByProductId(productId);
        assertEquals(expectedComments, actualComments);
    }
}