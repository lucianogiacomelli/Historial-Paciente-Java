package com.example.demo.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TurnoDTO {
    private LocalDateTime horario;
    private Long idPaciente;
    private Long idMedico;
    private Long idEspecialidad;
}
