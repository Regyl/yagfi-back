package com.github.regyl.gfi.exception;

public class RetryableException extends RuntimeException {

    public RetryableException(String message) {
        super(message);
    }

    public RetryableException(String message, Throwable cause) {
        super(message, cause);
    }

    public RetryableException(Throwable cause) {
        super(cause);
    }
}
