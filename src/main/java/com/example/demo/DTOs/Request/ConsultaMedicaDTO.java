package com.example.demo.DTOs.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaMedicaDTO {
    @NotBlank(message = "El motivo de la consulta no puede estar vacío.")
    @Size(max = 255, message = "El motivo no puede superar los 255 caracteres")
    private String motivo;

    @Size(max = 500, message = "Los síntomas no pueden superar los 500 caracteres")
    private String sintomas;

    @NotBlank(message = "El diagnóstico de la consulta no puede estar vacío.")
    @Size(max = 500, message = "El diagnóstico no puede superar los 500 caracteres")
    private String diagnostico;

    @NotBlank(message = "Las observaciones de la consulta no pueden estar vacías.")
    @Size(max = 1000, message = "Las observaciones no pueden superar los 1000 caracteres")
    private String observaciones;


    @Valid
    private List<TratamientoDTO> tratamientoDTOList;
}
