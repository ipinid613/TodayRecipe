package com.sparta.todayrecipe.repository;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByArticle_Id(Long articleId);
}