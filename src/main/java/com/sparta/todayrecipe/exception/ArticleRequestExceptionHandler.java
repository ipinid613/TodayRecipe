package com.sparta.todayrecipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ArticleRequestExceptionHandler {

    @ExceptionHandler(value = {ArticleRequestException.class})
    public ResponseEntity<Object> handleArticleRequestException(ArticleRequestException ex){
        ArticleException articleException = new ArticleException(ex.getMessage(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(articleException,HttpStatus.BAD_REQUEST);
    }
}