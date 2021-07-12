package com.sparta.todayrecipe.model;

import com.sparta.todayrecipe.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    public Comment(CommentRequestDto commentRequestDto, Article article, User user){
        this.content = commentRequestDto.getContent();
        this.article = article;
        this.user = user;
    }

}
