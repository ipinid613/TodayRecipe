package com.sparta.todayrecipe.model;

import com.sparta.todayrecipe.dto.ArticleRequestDto;
import com.sparta.todayrecipe.repository.ArticleRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
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

    public Article(ArticleRequestDto articleRequestDto) {
//        this.username = articleRequestDto.getUsername();
        this.title = articleRequestDto.getTitle();
        this.content = articleRequestDto.getContent();
        this.imageUrl = articleRequestDto.getImageUrl(); // 추가
    }

    public void update(ArticleRequestDto articleRequestDto) {
//        this.username = articleRequestDto.getUsername();
        this.title = articleRequestDto.getTitle();
        this.content = articleRequestDto.getContent();
        this.imageUrl = articleRequestDto.getImageUrl(); // 추가
    }
}
