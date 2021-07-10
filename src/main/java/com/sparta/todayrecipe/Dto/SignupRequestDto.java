package com.sparta.todayrecipe.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Setter
@Getter
public class SignupRequestDto {
    private Long id;

    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{3,12}",
            message = "유저명은 영문 대,소문자와 숫자 적어도 1개 이상씩 포함된 3자이사 12자 이하 여야 합니다.")
    @NotBlank(message = "유저명은 필수 입력 값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp="(?=\\S+$).{4,10}",
            message = "비밀번호는 4 ~ 10자의 비밀번호여야 합니다.")
    private String password;
    @NotBlank(message = "비밀번호는 확인이 필요합니다.")
    private String repassword;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    private boolean admin = false;

    private String adminToken = "";

}
