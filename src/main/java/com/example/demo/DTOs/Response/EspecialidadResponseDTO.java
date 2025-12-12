package com.example.demo.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class EspecialidadResponseDTO {
    private Long id;
    private String nombre;
}
