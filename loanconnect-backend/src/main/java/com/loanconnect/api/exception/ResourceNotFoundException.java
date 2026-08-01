package com.loanconnect.api.exception;

/** Thrown when a requested loan application does not exist. Mapped to 404 by GlobalExceptionHandler. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
