package com.sparta.todayrecipe.exception;


public class CommentRequestException extends IllegalArgumentException{

    public CommentRequestException(String message){
        super(message);
    }
}
