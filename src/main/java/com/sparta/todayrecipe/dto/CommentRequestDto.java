package com.sparta.todayrecipe.dto;

public class CommentRequestDto {
    private String content;

    public CommentRequestDto(String content) {
        this.content = content;
    }

    public CommentRequestDto() {
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
