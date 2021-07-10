package com.sparta.todayrecipe.service;

import com.sparta.todayrecipe.dto.CommentRequestDto;
import com.sparta.todayrecipe.dto.CommentResponseDto;
import com.sparta.todayrecipe.model.Article;
import com.sparta.todayrecipe.model.Comment;
import com.sparta.todayrecipe.model.User;
import com.sparta.todayrecipe.repository.ArticleRepository;
import com.sparta.todayrecipe.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public List<CommentResponseDto> getAllComments(Long articleId){
        //Article article =articleRepository.findById(articleId).orElse(null);
        List<Comment> comments = commentRepository.findByArticle_Id(articleId);

        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();

        for (Comment comment : comments){
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment.getId(), comment.getContent(), comment.getCreatedAt(), comment.getModifiedAt());
            commentResponseDtos.add(commentResponseDto);
        }

        return commentResponseDtos;


    }

    public Comment createComment(CommentRequestDto commentRequestDto, Long articleId, User user){
        Article article = articleRepository.findById(articleId).orElse(null);
        Comment comment = new Comment(commentRequestDto, article, user);
        commentRepository.save(comment);
        return comment;
    }


    //완료
    public void deleteComment(Long articleId, Long commentId, User user){
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new IllegalArgumentException("일치하는 댓글이 없습니다.")
        );
        if(!comment.getUser().equals(user)){
            throw new IllegalArgumentException("로그인 한 사용자와, 작성자가 다릅니다.");
        }

        commentRepository.delete(comment);
    }

}
