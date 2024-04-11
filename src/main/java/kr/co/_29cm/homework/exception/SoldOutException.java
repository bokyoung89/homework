package kr.co._29cm.homework.exception;

public class SoldOutException extends RuntimeException{
    public SoldOutException() {
        super();
    }

    public SoldOutException(String message) {
        super(message);
    }

    public SoldOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public SoldOutException(Throwable cause) {
        super(cause);
    }

    protected SoldOutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
