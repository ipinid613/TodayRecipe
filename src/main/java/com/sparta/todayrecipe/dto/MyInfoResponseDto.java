package com.sparta.todayrecipe.dto;

import com.sparta.todayrecipe.model.User;
import lombok.*;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class MyInfoResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String password;

    @Builder
    public MyInfoResponseDto(Long userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static MyInfoResponseDto of (User user) {
        return MyInfoResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}