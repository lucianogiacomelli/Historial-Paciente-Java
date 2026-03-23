package com.example.demo.Exception.Medico;

public class MedicoNotFoundException extends RuntimeException {
    public MedicoNotFoundException(Long id) {
        super("No existe un médico con el id: " + id);
    }
}
