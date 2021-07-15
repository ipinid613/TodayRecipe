package com.sparta.todayrecipe.controller;

import com.sparta.todayrecipe.dto.ArticleRequestDto;
import com.sparta.todayrecipe.dto.ArticleResponseDto;
import com.sparta.todayrecipe.exception.ArticleRequestException;
import com.sparta.todayrecipe.model.Article;
import com.sparta.todayrecipe.model.ArticleDetailResponse;
import com.sparta.todayrecipe.repository.ArticleRepository;
import com.sparta.todayrecipe.repository.UserRepository;
import com.sparta.todayrecipe.security.UserDetailsImpl;
import com.sparta.todayrecipe.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/api/articles/search")
    public List<ArticleResponseDto> getSearchedComments(@RequestParam("query") String keyword) {
        return articleService.getSearchedArticles(keyword);
    }

    ////////// READ //////////
    ///모든 게시물 조회///
    @GetMapping("/api/articles")
    public List<ArticleResponseDto> readArticle() {

        List<Article> articles = articleRepository.findAllByOrderByCreatedAtDesc();

        List<ArticleResponseDto> articleResponseDtos = new ArrayList<>();
        //계층 간 작업 시 Dto를 사용하는 습관을 갖는게 중요함.
        //Controller에서 직접 Article article을 건드리기보다 Dto를 활용하자.
        //효율성 측면에서도 좋음. Article 테이블(DB)에는 User의 정보 전부(id, username, password, email 등)가 연결되어있음.
        //내가 진짜 필요한 정보만 담아서 활용하는 것. User 전체가 아닌 User의 username만 뽑아서 쓰는 것이 효율적임.
        for(Article article : articles){
            ArticleResponseDto articleResponseDto = new ArticleResponseDto(
                    article.getId(),
                    article.getTitle(),
                    article.getUser().getUsername(), // <-- Dto 효율성의 좋은 예시
                    article.getContent(),
                    article.getCreatedAt(),
                    article.getModifiedAt(),
                    article.getImageUrl()
            );

            articleResponseDtos.add(articleResponseDto);
        }

        return articleResponseDtos;
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
            throw new ArticleRequestException("로그인 한 사용자만 쓰기 명령을 시도할 수 있습니다.");
        } // 로그인 한 user 정보가 없으면(token이 유효하지 않아서 @AuthenticationPrincipal이 먹히지 않는다면), Exception을 띄어줌.
        // IllegalArgumentException을 바로 띄우면 클라이언트 콘솔에 500에러를 출력함.
        // 로그인 안 한 사용자가 쓰기 명령을 하는 것은 클라이언트 오류이기 때문에 400에러로 변환하여 출력해줄 필요가 있음.
        // 때문에 ArticleRequestException을 생성했음.
        Article article = new Article(articleRequestDto, userDetails.getUser());
        return articleRepository.save(article);
    }

    ////////// UPDATE //////////
    @PutMapping("/api/articles/{id}") //특정 게시물 수정
    public Long updateArticle(@PathVariable Long id, @RequestBody ArticleRequestDto articleRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        // JWT 토큰 인증을 통해 userDetails가 생성되었을 경우에만 null이 아니게 됨. JWT 토큰 인증이 되지 않으면 null이 되므로 아래를 던짐.
        if(userDetails == null){
            throw new ArticleRequestException("로그인 한 사용자만 수정 명령을 시도할 수 있습니다.");
        }
        articleService.update(id, articleRequestDto, userDetails.getUser());
        return id;
    }

    @DeleteMapping("/api/articles/{id}")
    public void deleteArticle(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null){
            throw new ArticleRequestException("로그인 한 사용자만 삭제 명령을 시도할 수 있습니다.");
        }
        articleService.delete(id,userDetails.getUser());
    }
}
