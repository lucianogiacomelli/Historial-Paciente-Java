package com.example.demo.DTOs.Response;

import lombok.*;

@Data
@AllArgsConstructor
public class TratamientoResponseDTO {
    private String descripcion;
    private String dosis;
    private String frecuencia;
    private Integer duracionDias;
    private String indicaciones;
}
