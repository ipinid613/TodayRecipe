package com.sparta.todayrecipe.dto;

import com.sparta.todayrecipe.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MyInfoRequestDto {
    //    private final String username; // 삭제
    private final Long id;
    private final String password;

    @Builder
    public MyInfoRequestDto(Long id, String password){
        this.id = id;
        this.password = password;
    }

    public static MyInfoRequestDto a (User user){
        return MyInfoRequestDto.builder()
                .id(user.getId())
                .password(user.getPassword())
                .build();
    }
}