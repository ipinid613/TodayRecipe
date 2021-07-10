package com.sparta.todayrecipe.service;


import com.sparta.todayrecipe.dto.SignupRequestDto;
import com.sparta.todayrecipe.model.User;
import com.sparta.todayrecipe.model.UserRole;
import com.sparta.todayrecipe.repository.UserRepository;
import com.sparta.todayrecipe.security.UserDetailsImpl;
import com.sparta.todayrecipe.security.kakao.KakaoOAuth2;
import com.sparta.todayrecipe.security.kakao.KakaoUserInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

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
    private final AuthenticationManager authenticationManager;
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, KakaoOAuth2 kakaoOAuth2, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.kakaoOAuth2 = kakaoOAuth2;
        this.authenticationManager = authenticationManager;
    }

    // 회원가입 시, 유효성 체크
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            // errors.getFieldErrors() :
            // 입력 정보(아이디, 비밀번호, 비번 재확인, 이메일)의 유효성검사 결과에 대한 오류값(ex : 공백이 있다거나, 정규식 위반하는 경우들)
            // 여러 오류값들을 for문을 돌며 하나 하나씩 꺼냄(ex : 아이디 유효성검사 결과 오류 하나)
            String validKeyName = String.format("valid_%s", error.getField());
            // 하나의 오류값을 format처리하여 validKeyName에 저장. / ex) {valid_username : "유저명은 필수 입력 값입니다"}
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }

        return validatorResult; // 유효성검사 통과 시 {msg : null} 반환하고 db에 저장됨.
    }

    // 회원가입
    public String registerUser(@Valid SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String errorMessage = null;
        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            errorMessage = "중복된 사용자 ID 가 존재합니다.";
//            throw new IllegalArgumentException(errorMessage);
            return errorMessage;
        }
        // 패스워드 속에 아이디값 중복 없애기
        if(signupRequestDto.getPassword().contains(username) || username.contains(signupRequestDto.getPassword())) {
            errorMessage = "ID을 포함한 비번은 사용불가합니다.";
            return errorMessage;
        }
        if (!signupRequestDto.getPassword().equals(signupRequestDto.getRepassword())) {
            errorMessage = "비밀번호가 일치하지 않습니다..";
            return errorMessage;
        }

        // 패스워드 인코딩
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        // 사용자 ROLE 확인
        UserRole role = UserRole.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                errorMessage = "관리자 암호가 틀려 등록이 불가능합니다.";
//                throw new IllegalArgumentException(errorMessage);
                return errorMessage;
            }
            role = UserRole.ADMIN;
        }
        /// 위의 조건을 다 통과한 경우에 한해 userRepository.save 가능함 ///
        String email = signupRequestDto.getEmail();
        User user = new User(username, password, email, role);
        userRepository.save(user);
        return errorMessage;
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
                String password = kakaoId + ADMIN_TOKEN;
                // 패스워드 인코딩
                String encodedPassword = passwordEncoder.encode(password);
                // ROLE = 사용자
                UserRole role = UserRole.USER;

                kakaoUser = new User(username, encodedPassword, email, role, kakaoId);
                userRepository.save(kakaoUser);
            }
        }

        // 강제 로그인 처리
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

