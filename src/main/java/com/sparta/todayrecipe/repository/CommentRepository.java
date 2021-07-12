package com.sparta.todayrecipe.repository;

import com.sparta.todayrecipe.model.Article;
import com.sparta.todayrecipe.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByArticle_Id(Long articleId);
}
