package com.sparta.todayrecipe.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleResponseDto {
    private final Long id;
    private final String title;
    private final String username;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final String imageUrl;

    public ArticleResponseDto(Long id,String title,String username, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, String imageUrl){
        this.id = id;
        this.title = title;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.imageUrl = imageUrl;
    }
}
