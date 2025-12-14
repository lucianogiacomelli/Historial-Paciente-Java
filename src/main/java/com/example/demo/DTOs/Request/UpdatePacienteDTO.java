package com.example.demo.DTOs.Request;

import com.example.demo.Entities.Genero;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePacienteDTO {
    private String nombre;
    private String apellido;
    @Enumerated(EnumType.STRING)
    private Genero genero;
    private String numero;
    @Email(message = "El email debe tener un formato válido")
    private String email;
    private String direccion;

}
