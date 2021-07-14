package com.sparta.todayrecipe.service;

import com.sparta.todayrecipe.dto.ArticleRequestDto;
import com.sparta.todayrecipe.dto.ArticleResponseDto;
import com.sparta.todayrecipe.exception.ArticleRequestException;
import com.sparta.todayrecipe.model.Article;
import com.sparta.todayrecipe.model.ArticleDetailResponse;
import com.sparta.todayrecipe.model.User;
import com.sparta.todayrecipe.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional
    public Long update(Long id, ArticleRequestDto articleRequestDto, User user) {
        Article article = articleRepository.findById(id).orElseThrow(
                () -> new ArticleRequestException("requested id가 DB에 없습니다.")
        );

        if (!article.getUser().getId().equals(user.getId())) {
            throw new ArticleRequestException("로그인 한 사용자와 게시물 작성자가 다릅니다.");
        }

        article.setContent(articleRequestDto.getContent());
        article.setTitle(articleRequestDto.getTitle());
        article.setImageUrl(articleRequestDto.getImageUrl());
        articleRepository.save(article);
        return article.getId();
    }

    //@Transactional 안씀. DB의 내용을 조회하는 것이지 변경하는 과정이 없기 때문.
    public ArticleDetailResponse findById(Long id) {
        return articleRepository.findById(id) // findById를 통해 게시물을 찾고,
                .map(ArticleDetailResponse::of) // map() == 그 게시물에 ArticleDetailResponse를 적용하는데, 그 게시물의 내용은
                // 'of'로 연결된 부분의 ArticleDetailResponse다.
                // 그 내용이 DB에 입력된 안정적이고 변함없는 final한 값이어야 하므로 Article의 내용이어야 한다.
                // Dto나 그냥 ArticleDetailResponse면 안된다.
                // ArticleDetailResponse인데, of로 연결된 부분. 즉, Article.getId() 등등의 final한 내용을 받아와서 보여줘야 한다.
                .orElseThrow(() -> new ArticleRequestException("일치하는 게시물을 찾지 못했습니다."));
    }

    @Transactional
    public void delete(Long id, User user) {
        Article article = articleRepository.findById(id).orElseThrow(
                () -> new ArticleRequestException("requested id가 DB에 없습니다.")
        );

        if(!article.getUser().getId().equals(user.getId())){
            throw new ArticleRequestException("로그인 한 사용자와 게시글 작성자가 다릅니다.");
        }

        articleRepository.delete(article);
    }


    public List<ArticleResponseDto> getSearchedArticles(String keyword) {
        List<Article> articles = articleRepository.findByTitleContaining(keyword);
        List<ArticleResponseDto> articleResponseDtos = new ArrayList<>();

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
}
