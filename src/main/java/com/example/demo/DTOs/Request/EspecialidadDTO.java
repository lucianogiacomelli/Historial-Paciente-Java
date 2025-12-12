package com.example.demo.DTOs.Request;

import jakarta.validation.constraints.NotBlank;
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

}
