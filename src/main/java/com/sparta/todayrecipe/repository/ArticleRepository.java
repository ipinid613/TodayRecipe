package com.sparta.todayrecipe.repository;

import com.sparta.todayrecipe.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article,Long> {

}
