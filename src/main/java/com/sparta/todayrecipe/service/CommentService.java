package com.sparta.todayrecipe.service;

import com.sparta.todayrecipe.dto.CommentRequestDto;
import com.sparta.todayrecipe.model.Article;
import com.sparta.todayrecipe.model.Comment;
import com.sparta.todayrecipe.repository.ArticleRepository;
import com.sparta.todayrecipe.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public List<Comment> getAllComments(Long articleId){
        Article article =articleRepository.findById(articleId).orElse(null);

        return commentRepository.findByArticle(article);
    }

    public Comment createComment(CommentRequestDto commentRequestDto, Long articleId){
        Article article = articleRepository.findById(articleId).orElse(null);
        Comment comment = new Comment(commentRequestDto, article);
        commentRepository.save(comment);
        return comment;
    }
}
