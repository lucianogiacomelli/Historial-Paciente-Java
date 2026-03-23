package com.example.demo.DTOs.Response;

import com.example.demo.Entities.EstadoTurno.EstadoTurno;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TurnoEstadoResponseDTO {
    private EstadoTurno estado;
    private LocalDateTime fecha;
}
