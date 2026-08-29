package com.github.regyl.gfi.exception;

public class RateLimitExceedException extends RetryableException {

    public RateLimitExceedException(String message) {
        super(message);
    }
}
