package com.sparta.todayrecipe.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;



@Setter
@Getter
public class SignupRequestDto {
    private Long id;

    @NotBlank(message = "유저명은 필수 입력 값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
    @NotBlank(message = "비밀번호는 확인이 필요합니다.")
    private String repassword;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    private boolean admin = false;

    private String adminToken = "";

}
