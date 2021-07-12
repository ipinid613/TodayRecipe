package com.sparta.todayrecipe.exception;

import com.sparta.todayrecipe.dto.CommentRequestDto;
import com.sparta.todayrecipe.dto.SignupRequestDto;

public class CommentRequestException extends IllegalArgumentException{

    public CommentRequestException(String message){
        super(message);
    }
}
