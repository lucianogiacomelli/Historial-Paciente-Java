package com.example.demo.DTOs.Response;

import com.example.demo.Entities.Genero;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PacienteResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaNacimiento;
    @Enumerated(EnumType.STRING)
    private Genero genero;
    private String numero;
    private String email;
    private String direccion;

}
