package com.sparta.todayrecipe.repository;

import com.sparta.todayrecipe.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByOrderByModifiedAtDesc();

    List<Article> findByTitleContaining(String keyword);

    List<Article> findByTitleIsContaining(String keyword);

    List<Article> findByTitleContains(String keyword);

    List<Article> findByTitleLike(String wildCard);
}
