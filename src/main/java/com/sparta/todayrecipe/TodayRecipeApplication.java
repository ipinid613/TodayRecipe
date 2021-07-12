package com.sparta.todayrecipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableJpaAuditing // timestamped 관련 필수 어노테이션
@SpringBootApplication
public class TodayRecipeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodayRecipeApplication.class, args);
    }

    @PostConstruct
    public void started(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

}
