package com.example.demo.Exception.Medico;

public class MedicoInvalidoException extends RuntimeException {
    public MedicoInvalidoException(String message) {
        super(message);
    }
}
