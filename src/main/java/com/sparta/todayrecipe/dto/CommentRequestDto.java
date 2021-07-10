package com.sparta.todayrecipe.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CommentRequestDto {
    private final String content;
}
