package com.example.demo.DTOs.Request;

import jakarta.validation.constraints.Email;
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
public class UpdateMedicoDTO {

    private String nombre;
    private String apellido;
    private String telefono;
    private List<Long> especialidadDTOList;
}
