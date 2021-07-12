package com.sparta.todayrecipe.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAuthResponse {
    private final String username;
    public boolean isLogin(){
        return !this.username.contains("ANONYMOUS");
    }

    public static UserAuthResponse empty(){
        return UserAuthResponse.builder()
                .username("ANONYMOUS")
                .build();
    }
}
