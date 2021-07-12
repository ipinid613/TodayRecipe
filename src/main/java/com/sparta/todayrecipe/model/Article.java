package com.sparta.todayrecipe.model;

import com.sparta.todayrecipe.dto.ArticleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Article extends Timestamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

//    @Column(nullable = false)
//    private String username;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false) // 추가
    private String imageUrl;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    public Article(ArticleRequestDto articleRequestDto, User user) {
//        this.username = articleRequestDto.getUsername();
        this.title = articleRequestDto.getTitle();
        this.content = articleRequestDto.getContent();
        this.imageUrl = articleRequestDto.getImageUrl(); // 추가
        this.user = user;
    }

//    public void update(ArticleRequestDto articleRequestDto) {
////        this.username = articleRequestDto.getUsername();
//        this.title = articleRequestDto.getTitle();
//        this.content = articleRequestDto.getContent();
//        this.imageUrl = articleRequestDto.getImageUrl(); // 추가
//    }
}