package com.sparta.todayrecipe.service;

import com.sparta.todayrecipe.dto.CommentRequestDto;
import com.sparta.todayrecipe.dto.CommentResponseDto;
import com.sparta.todayrecipe.exception.CommentRequestException;
import com.sparta.todayrecipe.model.Article;
import com.sparta.todayrecipe.model.Comment;
import com.sparta.todayrecipe.model.User;
import com.sparta.todayrecipe.repository.ArticleRepository;
import com.sparta.todayrecipe.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public List<CommentResponseDto> getAllComments(Long articleId) {
        //Article article =articleRepository.findById(articleId).orElse(null);
        List<Comment> comments = commentRepository.findByArticle_Id(articleId);

        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(
                    comment.getId(),
                    comment.getContent(),
                    comment.getCreatedAt(),
                    comment.getModifiedAt(),
                    comment.getUser().getUsername()
            );
            commentResponseDtos.add(commentResponseDto);
        }

        return commentResponseDtos;


    }

    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long articleId, User user) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new CommentRequestException("requested articleId가 DB에 없습니다.")
        );
        Comment comment = new Comment(commentRequestDto, article, user);
        commentRepository.save(comment);

        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt(),
                comment.getUser().getUsername()
        );
    }


    //완료
    @Transactional
    public void deleteComment(Long articleId, Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentRequestException("requested commentId가 DB에 없습니다.")
        );

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentRequestException("로그인 한 사용자와, 댓글 작성자가 다릅니다.");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public void updateComment(CommentRequestDto commentRequestDto, Long articleId, Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new CommentRequestException("requested commentId가 DB에 없습니다.")
        );

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentRequestException("로그인 한 사용자와, 댓글 작성자가 다릅니다.");
        }

        comment.setContent(commentRequestDto.getContent());
        commentRepository.save(comment);
    }
}