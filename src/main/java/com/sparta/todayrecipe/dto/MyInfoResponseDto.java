package com.sparta.todayrecipe.dto;

import com.sparta.todayrecipe.model.User;
import lombok.*;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class MyInfoResponseDto {
    private String username;
    private String email;

    @Builder
    public MyInfoResponseDto(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public static MyInfoResponseDto of (User user) {
        return MyInfoResponseDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}