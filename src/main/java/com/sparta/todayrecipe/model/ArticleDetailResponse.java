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
//    private String username; // 삭제
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String imageUrl; // 추가

    @Builder
    public ArticleDetailResponse(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, String imageUrl){
        this.id = id;
//        this.username = username; // 삭제
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.imageUrl = imageUrl; // 추가
    }

    public static ArticleDetailResponse of (Article article) {
                        // of ==  ArticleDetailResponse findById에 연결된 부분 //
        return ArticleDetailResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
//                .username(article.getUsername()) // 삭제
                .content(article.getContent())
                .createdAt(article.getCreatedAt())
                .modifiedAt(article.getModifiedAt())
                .imageUrl(article.getImageUrl())
                .build();
    }
}
