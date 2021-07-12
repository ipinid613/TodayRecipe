package com.sparta.todayrecipe.exception;

public class ArticleRequestException extends IllegalArgumentException{

    public ArticleRequestException(String message){
        super(message);
    }
}