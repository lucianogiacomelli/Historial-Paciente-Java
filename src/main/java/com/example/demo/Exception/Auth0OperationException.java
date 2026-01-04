package com.example.demo.Exception;

public class Auth0OperationException extends RuntimeException {
    public Auth0OperationException(String message) {
        super(message);
    }
}
