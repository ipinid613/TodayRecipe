package com.sparta.todayrecipe.controller;

import com.sparta.todayrecipe.dto.ArticleRequestDto;
import com.sparta.todayrecipe.model.Article;
import com.sparta.todayrecipe.model.ArticleDetailResponse;
import com.sparta.todayrecipe.model.User;
import com.sparta.todayrecipe.repository.ArticleRepository;
import com.sparta.todayrecipe.repository.UserRepository;
import com.sparta.todayrecipe.security.UserDetailsImpl;
import com.sparta.todayrecipe.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final UserRepository userRepository;
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
    public Article createArticle(@RequestBody ArticleRequestDto articleRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if(userDetails == null){
            throw new IllegalArgumentException("로그인 한 사용자만 쓰기 명령을 시도할 수 있습니다.");
        }
        Article article = new Article(articleRequestDto, userDetails.getUser());
        return articleRepository.save(article);
    }

    ////////// UPDATE //////////
    @PutMapping("/api/articles/{id}") //특정 게시물 수정
    public Long updateArticle(@PathVariable Long id, @RequestBody ArticleRequestDto articleRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if(userDetails == null){
            throw new IllegalArgumentException("로그인 한 사용자만 수정 명령을 시도할 수 있습니다.");
        }
        articleService.update(id, articleRequestDto, userDetails.getUser());
        return id;
    }

    @DeleteMapping("/api/articles/{id}")
    public void deleteArticle(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null){
            throw new IllegalArgumentException("로그인 한 사용자만 삭제 명령을 시도할 수 있습니다.");
        }
        articleService.delete(id,userDetails.getUser());
    }
}
