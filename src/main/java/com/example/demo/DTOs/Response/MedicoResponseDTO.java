package com.example.demo.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MedicoResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String matricula;
    private List<String> especialidades;
}

