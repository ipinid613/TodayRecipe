package com.sparta.todayrecipe.model;


import lombok.*;

import javax.persistence.*;

@Setter
@Getter // get 함수를 일괄적으로 만들어줍니다.
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends Timestamped {

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.kakaoId = null;
    }

    public User(String username, String password, String email, Long kakaoId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.kakaoId = kakaoId;
    }


    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    // 반드시 값을 가지도록 합니다.
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private Long kakaoId;

}