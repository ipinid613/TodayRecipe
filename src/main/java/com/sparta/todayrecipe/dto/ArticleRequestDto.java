package com.sparta.todayrecipe.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ArticleRequestDto {
//    private final String username; // 삭제
    private final String title;
    private final String content;
    private final String imageUrl;
}
