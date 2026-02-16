package com.example.demo.DTOs.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TratamientoDTO {
    @NotBlank(message = "La descripción del tratamiento es obligatoria")
    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
    private String descripcion;

    @Size(max = 100, message = "La dosis no puede superar los 100 caracteres")
    private String dosis;

    @Size(max = 100, message = "La frecuencia no puede superar los 100 caracteres")
    private String frecuencia;

    @Min(value = 1, message = "La duración debe ser mayor a 0 días")
    private Integer duracionDias;

    @Size(max = 500, message = "Las indicaciones no pueden superar los 500 caracteres")
    private String indicaciones;
}
