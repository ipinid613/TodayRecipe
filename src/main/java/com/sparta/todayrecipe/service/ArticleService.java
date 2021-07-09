package com.sparta.todayrecipe.service;

import com.sparta.todayrecipe.dto.ArticleRequestDto;
import com.sparta.todayrecipe.model.Article;
import com.sparta.todayrecipe.model.ArticleDetailResponse;
import com.sparta.todayrecipe.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public Long update(Long id,ArticleRequestDto articleRequestDto) {
        Article article = articleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("일치하는 게시글 고유아이디가 존재하지 않습니다.")
        );
        article.update(articleRequestDto);
        return article.getId();
    }

    @Transactional
    public ArticleDetailResponse findById(Long id) {
        return articleRepository.findById(id)
                .map(ArticleDetailResponse::of)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 게시물을 찾지 못했습니다."));
    }
}
