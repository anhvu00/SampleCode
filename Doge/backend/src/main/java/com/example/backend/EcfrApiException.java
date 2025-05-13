package com.example.backend;

public class EcfrApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EcfrApiException(String message) {
        super(message);
    }

    public EcfrApiException(String message, Throwable cause) {
        super(message, cause);
    }
}