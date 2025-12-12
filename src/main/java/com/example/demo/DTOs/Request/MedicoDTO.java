package com.example.demo.DTOs.Request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MedicoDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String apellido;

    @NotBlank(message = "La matrícula no puede estar vacía")
    private String matricula;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener 10 dígitos numéricos")
    private String telefono;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @Size(min = 7, max = 8, message = "El DNI debe tener entre 7 y 8 caracteres")
    @NotBlank(message = "El DNI no puede estar vacío")
    private String dni;

    @NotBlank(message = "El password no puede estar vacío")
    private String password;

    @NotEmpty(message = "Debe seleccionar al menos una especialidad")
    @Valid
    private List<Long> especialidadDTOList = new ArrayList<>();
}
