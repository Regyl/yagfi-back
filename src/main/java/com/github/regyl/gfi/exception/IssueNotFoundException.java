package com.github.regyl.gfi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Αυτή η κλάση λέει στη Spring να επιστρέφει 404 Not Found
 * αντί για 500 Internal Server Error.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class IssueNotFoundException extends RuntimeException {
    public IssueNotFoundException(String message) {
        super(message);
    }
}