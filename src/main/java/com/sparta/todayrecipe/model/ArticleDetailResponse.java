package com.sparta.todayrecipe.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@Getter
public class ArticleDetailResponse {
    private Long id;
    private String username;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public ArticleDetailResponse(Long id, String username, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt){
        this.id = id;
        this.username = username;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static ArticleDetailResponse of (Article article) {
                        // of ==  ArticleDetailResponse findById에 연결된 부분 //
        return ArticleDetailResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .username(article.getUsername())
                .content(article.getContent())
                .createdAt(article.getCreatedAt())
                .modifiedAt(article.getModifiedAt())
                .build();
    }
}
