package com.example.demo.Exception;

public class ResourceInvalidException extends RuntimeException {
    public ResourceInvalidException(String message) {
        super(message);
    }
}
