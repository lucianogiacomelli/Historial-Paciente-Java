package com.example.demo.Exception;

public class Auth0OperationException extends RuntimeException {
    public Auth0OperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
