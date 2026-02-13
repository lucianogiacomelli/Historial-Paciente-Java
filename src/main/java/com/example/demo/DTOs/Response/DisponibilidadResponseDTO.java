package com.example.demo.DTOs.Response;


import com.example.demo.Entities.Dias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@Builder
public class DisponibilidadResponseDTO {
    private Long id;
    private Dias diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Long medicoId;
    private String nombreMedico;
    private Long especialidadId;
    private String especialidadNombre;
    private Integer duracionTurno;

}
