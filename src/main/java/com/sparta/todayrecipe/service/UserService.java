package com.sparta.todayrecipe.service;


import com.sparta.todayrecipe.dto.SignupRequestDto;
import com.sparta.todayrecipe.exception.UserRequestException;
import com.sparta.todayrecipe.model.User;
import com.sparta.todayrecipe.repository.UserRepository;
import com.sparta.todayrecipe.security.UserDetailsImpl;
import com.sparta.todayrecipe.security.kakao.KakaoOAuth2;
import com.sparta.todayrecipe.security.kakao.KakaoUserInfo;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KakaoOAuth2 kakaoOAuth2;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, KakaoOAuth2 kakaoOAuth2) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kakaoOAuth2 = kakaoOAuth2;
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
            errorMessage = "중복된 email이 존재합니다.";
            throw new UserRequestException(errorMessage);
        }
        User user = new User(username, password, email);
        userRepository.save(user);

    }

    public void kakaoLogin(String authorizedCode) {
        // 카카오 OAuth2 를 통해 카카오 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String nickname = userInfo.getNickname();
        String email = userInfo.getEmail();

        // DB 에 중복된 Kakao Id 가 있는지 확인
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);

        if (kakaoUser == null) {
            // 카카오 이메일과 동일한 이메일을 가진 회원이 있는지 확인
            User sameEmailUser = userRepository.findByEmail(email).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 카카오 이메일과 동일한 이메일 회원이 있는 경우
                // 카카오 Id 를 회원정보에 저장
                kakaoUser.setKakaoId(kakaoId);
                userRepository.save(kakaoUser);

            } else {
                // 카카오 정보로 회원가입
                // username = 카카오 nickname
                String username = nickname;
                // password = 카카오 Id + ADMIN TOKEN
                String password = kakaoId + "dkjflkdjfioepfjdkjdflsl4ljssdkljf";
                // 패스워드 인코딩
                String encodedPassword = passwordEncoder.encode(password);
                // ROLE = 사용자

                kakaoUser = new User(username, encodedPassword, email, kakaoId);
                userRepository.save(kakaoUser);
            }
        }
        // 강제 로그인 처리
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

