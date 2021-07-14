package com.sparta.todayrecipe.controller;


import com.sparta.todayrecipe.dto.MyInfoResponseDto;
import com.sparta.todayrecipe.exception.UserRequestException;
import com.sparta.todayrecipe.security.UserDetailsImpl;
import com.sparta.todayrecipe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MyInfoContoller {
    private final UserService userService;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    //유저 정보 페이지
    @GetMapping("/myinfo")
    public ResponseEntity<MyInfoResponseDto> findById(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new UserRequestException("로그인 한 사용자만 유저 정보를 볼 수 있습니다.");
        }
        MyInfoResponseDto myInfoResponseDto = MyInfoResponseDto.of(userDetails.getUser());

        return ResponseEntity.ok(myInfoResponseDto);
    }

    //비밀번호 수정 전, 현재 설정된 비밀번호가 맞는지 확인
    @PostMapping("/myinfo")
    public Map<String, String> checkPassword(@RequestBody Map<String, String> password, @AuthenticationPrincipal UserDetailsImpl userDetails) {


        if (userDetails == null) {
            throw new UserRequestException("로그인 한 사용자만 비밀번호 체크를 할 수 있습니다.");
        }
        String dbPassword = userDetails.getPassword();

        String check = password.get("password");

        if (!passwordEncoder.matches(check, dbPassword)) {
            throw new UserRequestException("현재 비밀번호와 일치하지 않습니다.");
        }

        Map<String, String> result = new HashMap<>();

        result.put("result", "현재 비밀번호와 일치합니다");

        return result;
    }

    ////// 유저 비밀번호 변경 요청 //////
    @PutMapping("/myinfo")
    public ResponseEntity<MyInfoResponseDto> editPassword(@RequestBody Map<String, String> newPassword, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new UserRequestException("로그인 한 사용자만 비밀번호 변경을 할 수 있습니다.");
        }

        Map<String, String> result = new HashMap<>();
        String newPass = newPassword.get("newpassword");
        String rePass = newPassword.get("renewpassword");
        if (!newPass.equals(rePass)) {
            throw new UserRequestException("비밀번호가 서로 일치하지 않습니다.");
        }
//        result.put("result", "비밀번호 변경이 완료되었습니다.");
        userService.update(newPass, userDetails.getUser());
//        return result;

        MyInfoResponseDto myInfoResponseDto = MyInfoResponseDto.of(userDetails.getUser());

        return ResponseEntity.ok(myInfoResponseDto);

    }
}