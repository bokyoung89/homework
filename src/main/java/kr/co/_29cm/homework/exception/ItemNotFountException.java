package kr.co._29cm.homework.exception;

import lombok.Getter;

@Getter
public class ItemNotFountException extends RuntimeException {
    private String message;

    public ItemNotFountException(String message) {
        this.message = message;
    }
}
