package com.sparta.todayrecipe.controller;


import com.sparta.todayrecipe.dto.SignupRequestDto;
import com.sparta.todayrecipe.exception.UserRequestException;
import com.sparta.todayrecipe.model.User;
import com.sparta.todayrecipe.repository.UserRepository;
import com.sparta.todayrecipe.security.JwtTokenProvider;

import com.sparta.todayrecipe.service.UserService;
import lombok.RequiredArgsConstructor;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성합니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public void registerUser(@Valid @RequestBody SignupRequestDto signupRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            userService.validateHandling(errors);
        }
        userService.registerUser(signupRequestDto);
    }


    // 로그인
    @PostMapping("/user/login")
    public List<Map<String,String>> login(@RequestBody Map<String, String> user) {
        User member = userRepository.findByUsername(user.get("username"))
                .orElseThrow(() -> new UserRequestException("가입되지 않은 username 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new UserRequestException("잘못된 비밀번호입니다.");
        }
        Map<String,String>username =new HashMap<>();
        Map<String,String>token = new HashMap<>();
        List<Map<String,String>> tu = new ArrayList<>(); // -> 리스트를 만드는데, Map형태(키:밸류 형태)의 변수들을 담을 것이다.
        token.put("token",jwtTokenProvider.createToken(member.getUsername(), member.getEmail())); // "username" : {username}
        username.put("username",member.getUsername()); // "token" : {token}
        tu.add(username); //List형태 ["username" : {username}]
        tu.add(token); //List형태 ["token" : {token}]
        return tu; // List형태 ["username" : {username}, "token" : {token}]
    }
}
