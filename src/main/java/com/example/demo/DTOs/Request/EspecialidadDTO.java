package com.example.demo.DTOs.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EspecialidadDTO {

    @NotBlank(message = "El nombre de la especialidad no puede estar vacío")
    private String nombre;

    @NotNull(message = "La duración del turno es obligatoria")
    @Min(value = 5, message = "La duración mínima es de 5 minutos")
    @Max(value = 180, message = "La duración máxima es de 180 minutos")
    private Integer duracionTurno;


}
