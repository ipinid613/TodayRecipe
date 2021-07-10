package com.sparta.todayrecipe.model;

import com.sparta.todayrecipe.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Comment {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Article article;

    public Comment(CommentRequestDto commentRequestDto, Article article, User user){
        this.user = user;
        this.content = commentRequestDto.getContent();
        this.article = article;
    }
}
