package com.sparta.todayrecipe.controller;


import com.sparta.todayrecipe.Dto.SignupRequestDto;

import com.sparta.todayrecipe.service.UserService;
import lombok.RequiredArgsConstructor;


import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성합니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
public class UserController {

    private final UserService userService;

    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public Map registerUser(@Valid @RequestBody SignupRequestDto signupRequestDto, Errors errors) {
        Map<String, String> user = new HashMap<>();
        if (errors.hasErrors()) {

            // 유효성 통과 못한 필드와 메시지를 핸들링/// 갓 선용 !!!!!!!!!
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                user.put(key, validatorResult.get(key));
            }
            return user;

        }

        user.put("msg",userService.registerUser(signupRequestDto));

        return user;
    }

    // 카카오톡
    @GetMapping("/user/kakao/callback")
    public void kakaoLogin(String code) {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        userService.kakaoLogin(code);

    }

}
