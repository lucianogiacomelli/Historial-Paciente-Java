package com.example.demo.DTOs.Response;

import com.example.demo.Entities.EstadoTurno.EstadoTurno;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TurnoResponseDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoTurno estado;

    private Long medicoId;
    private String medicoNombreCompleto;

    private Long pacienteId;
    private String pacienteNombreCompleto;

    private Long especialidadId;
    private String especialidadNombre;

    private List<TurnoEstadoResponseDTO> historialEstados;


}
