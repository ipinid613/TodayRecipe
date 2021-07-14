package com.sparta.todayrecipe.controller;

import com.sparta.todayrecipe.dto.MyInfoResponseDto;
import com.sparta.todayrecipe.exception.UserRequestException;
import com.sparta.todayrecipe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MyInfoContoller {
    private final UserService userService;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    //유저 정보 페이지
    @GetMapping("/myinfo/{userId}")
    public ResponseEntity<MyInfoResponseDto> findById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    //비밀번호 수정 전, 현재 설정된 비밀번호가 맞는지 확인
    @PostMapping("/myinfo/{userId}")
    public String checkPassword(@PathVariable Long userId, @RequestBody Map<String, String> password) {

        String result = null;

        String dbPassword = userService.checkPassword(userId).getPassword();

        String check = password.get("password");

        if (!passwordEncoder.matches(check, dbPassword)) {
            throw new UserRequestException("현재 비밀번호와 일치하지 않습니다.");
        }
        result = "현재 비밀번호와 일치합니다.";
    return result;
    }
}