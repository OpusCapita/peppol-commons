package com.opuscapita.peppol.commons.auth;

import org.springframework.http.HttpStatus;

public class AuthServiceException extends RuntimeException {

    private HttpStatus status;

    private String message;

    public AuthServiceException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public AuthServiceException() {
        this(HttpStatus.BAD_REQUEST);
    }

    public AuthServiceException(HttpStatus status) {
        this(status, "unknown");
    }

    public AuthServiceException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
