package com.github.regyl.gfi.exception;

public class EventNotFoundException extends NonRetryableException {

    public EventNotFoundException(String message) {
        super(message);
    }
}
