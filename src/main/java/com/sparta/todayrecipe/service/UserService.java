package com.sparta.todayrecipe.service;


import com.sparta.todayrecipe.dto.SignupRequestDto;
import com.sparta.todayrecipe.exception.UserRequestException;
import com.sparta.todayrecipe.model.User;
import com.sparta.todayrecipe.repository.UserRepository;
import com.sparta.todayrecipe.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 시, 유효성 체크
    public void validateHandling(Errors errors) {
        String errorMessage;
        for (FieldError error : errors.getFieldErrors()) {
            errorMessage = error.getField();
            throw new UserRequestException(errorMessage);
        }
    }

    // 회원가입
    public void registerUser(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String errorMessage;
        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            errorMessage = "중복된 사용자 ID 가 존재합니다.";
            throw new UserRequestException(errorMessage);

        }
        // 패스워드 속에 아이디값 중복 없애기
        if(signupRequestDto.getPassword().contains(username) || username.contains(signupRequestDto.getPassword())) {
            errorMessage = "ID을 포함한 비번은 사용불가합니다.";
            throw new UserRequestException(errorMessage);
        }
        if (!signupRequestDto.getPassword().equals(signupRequestDto.getRepassword())) {
            errorMessage = "비밀번호가 일치하지 않습니다.";
            throw new UserRequestException(errorMessage);
        }

        // 패스워드 인코딩
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        /// 위의 조건을 다 통과한 경우에 한해 userRepository.save 가능함 ///
        String email = signupRequestDto.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            errorMessage = "중복된 email 이 존재합니다.";
            throw new UserRequestException(errorMessage);
        }
        User user = new User(username, password, email);
        userRepository.save(user);

    }
}