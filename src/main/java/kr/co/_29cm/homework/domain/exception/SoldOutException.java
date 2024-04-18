package kr.co._29cm.homework.domain.exception;

import lombok.Getter;

@Getter
public class SoldOutException extends RuntimeException{
    private String message;

    public SoldOutException(String message){
        this.message = message;
    }
}
