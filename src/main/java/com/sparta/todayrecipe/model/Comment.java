package com.sparta.todayrecipe.model;

import com.sparta.todayrecipe.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class Comment extends Timestamped{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Article article;

    public Comment(CommentRequestDto commentRequestDto, Article article){
        this.content = commentRequestDto.getContent();
        this.article = article;
    }

}
