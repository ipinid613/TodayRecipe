package com.sparta.todayrecipe.controller;

import com.sparta.todayrecipe.dto.CommentRequestDto;
import com.sparta.todayrecipe.model.Comment;
import com.sparta.todayrecipe.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/articles/{articleId}/comments")
    public List<Comment> getAllComments(@PathVariable Long articleId){
        return commentService.getAllComments(articleId);
    }

    @PostMapping("/api/articles/{articleId}/comments")
    public Comment createComment(@PathVariable Long articleId, @RequestBody CommentRequestDto commentRequestDto,  @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        Comment comment = commentService.createComment(commentRequestDto,user,articleId);
        return comment;
    }
}
