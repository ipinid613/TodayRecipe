package com.sparta.todayrecipe.controller;

import com.sparta.todayrecipe.dto.CommentRequestDto;
import com.sparta.todayrecipe.model.Comment;
import com.sparta.todayrecipe.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.SwaggerDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "CommentController V1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ApiOperation("게시물의 댓글 조회")
    @GetMapping("/api/articles/{articleId}/comments")
    public List<Comment> getAllComments(
            @ApiParam("게시물 아이디")
            @PathVariable Long articleId){
        return commentService.getAllComments(articleId);
    }

    @ApiOperation("게시물의 댓글 작성")
    @PostMapping("/api/articles/{articleId}/comments")
    public Comment createComment(@PathVariable Long articleId, @RequestBody CommentRequestDto commentRequestDto){
        Comment comment = commentService.createComment(commentRequestDto,articleId);
        return comment;
    }
}
