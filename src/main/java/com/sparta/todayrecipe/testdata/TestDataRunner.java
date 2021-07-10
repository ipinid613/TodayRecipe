package com.sparta.todayrecipe.testdata;

import com.sparta.todayrecipe.model.User;
import com.sparta.todayrecipe.model.UserRole;
import com.sparta.todayrecipe.repository.ArticleRepository;
import com.sparta.todayrecipe.repository.UserRepository;
import com.sparta.todayrecipe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    UserService userService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception{
        //테스트 User 생성
//        User testUser = new User("이태강", passwordEncoder.encode("123"),"lkorea12@naver.com", UserRole.USER);
//        testUser = userRepository.save(testUser);
    }
}
