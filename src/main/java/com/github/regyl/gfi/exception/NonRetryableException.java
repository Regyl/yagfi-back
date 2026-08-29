package com.github.regyl.gfi.exception;

public class NonRetryableException extends RuntimeException {

    public NonRetryableException(String message) {
        super(message);
    }

    public NonRetryableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonRetryableException(Throwable cause) {
        super(cause);
    }
}
