package kr.co._29cm.homework.domain.exception;

import lombok.Getter;

@Getter
public class ItemNotFoundException extends RuntimeException {
    private String message;

    public ItemNotFoundException(String message) {
        this.message = message;
    }
}
