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
@RestController // @Controller + @ResponseBody
// RestController는 JSON형태로의 반환을 위해서 필요
// Controller는 뷰(text/html 타입의 응답, ex - index, register ...)를 반환하는 것.
public class ArticleController {

    // 의존성 주입(?) //
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    // ArticleRequestDto는 의존성이 필요한게 아니고 단순히 파라미터에 들어가는 내용이기 때문에 이곳에 작성하지 않음.

    ////////// READ //////////
    ///모든 게시물 조회///
    @GetMapping("/api/articles")
    public List<Article> readArticle() {
        return articleRepository.findAllByOrderByModifiedAtDesc();
    }
    ///특정 게시물 조회///
    // ResponseEntity = HttpRequest에 대한 응답 데이터 포함하는 클래스.
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleDetailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.findById(id));
    } // HttpStatus 코드의 OK(200)을 응답데이터에 포함하고, 그 때 게시글번호가 일치하는 하나의 게시물을 JSON형태로 반환함.

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
