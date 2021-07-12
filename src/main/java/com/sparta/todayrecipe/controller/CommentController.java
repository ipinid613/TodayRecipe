package com.sparta.todayrecipe.controller;

import com.sparta.todayrecipe.dto.CommentRequestDto;
import com.sparta.todayrecipe.dto.CommentResponseDto;
import com.sparta.todayrecipe.exception.CommentRequestException;
import com.sparta.todayrecipe.security.UserDetailsImpl;
import com.sparta.todayrecipe.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "CommentController V1")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ApiOperation("게시물의 댓글 조회")
    @GetMapping("/api/articles/{articleId}/comments")
    public List<CommentResponseDto> getAllComments(
            @ApiParam("게시물 아이디")
            @PathVariable Long articleId) {
        return commentService.getAllComments(articleId);
    }

    @ApiOperation("게시물의 댓글 작성")
    @PostMapping("/api/articles/{articleId}/comments")
    public void createComment(@PathVariable Long articleId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CommentRequestException("로그인을 해야 댓글을 작성할 수 있습니다.");
        }
        commentService.createComment(commentRequestDto, articleId, userDetails.getUser());
    }

    @ApiOperation("게시물의 댓글 삭제")
    @DeleteMapping("/api/articles/{articleId}/comments/{commentId}")
    public void deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails==null){
            throw new CommentRequestException("로그인을 해야 댓글을 삭제할 수 있습니다.");
        }
        commentService.deleteComment(articleId, commentId, userDetails.getUser());

    }

    @ApiOperation("게시물의 댓글 수정")
    @PutMapping("/api/articles/{articleId}/comments/{commentId}")
    public void updateComment(@RequestBody CommentRequestDto commentRequestDto, @PathVariable Long articleId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails==null){
            throw new CommentRequestException("로그인을 해야 댓글을 수정할 수 있습니다.");
        }
        commentService.updateComment(commentRequestDto, articleId, commentId, userDetails.getUser());
    }
}
