package com.sparta.todayrecipe.controller;

import com.sparta.todayrecipe.dto.ArticleRequestDto;
import com.sparta.todayrecipe.model.Article;
import com.sparta.todayrecipe.model.ArticleDetailResponse;
import com.sparta.todayrecipe.repository.ArticleRepository;
import com.sparta.todayrecipe.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ArticleController {

    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    ////////// READ //////////
    ///모든 게시물 조회///
    @GetMapping("/api/articles")
    public List<Article> readArticle() {
        return articleRepository.findAllByOrderByModifiedAtDesc();
    }
    ///특정 게시물 조회///
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleDetailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.findById(id));
    }

    ////////// CREATE //////////
    @PostMapping("/api/articles") //게시물 작성
    public Article createArticle(@RequestBody ArticleRequestDto articleRequestDto) {
        Article article = new Article(articleRequestDto);
        return articleRepository.save(article);
    }

    ////////// UPDATE //////////
    @PutMapping("/api/articles/{id}") //특정 게시물 수정
    public Long updateArticle(@PathVariable Long id, @RequestBody ArticleRequestDto articleRequestDto) {
        articleService.update(id, articleRequestDto);
        return id;
    }

}
