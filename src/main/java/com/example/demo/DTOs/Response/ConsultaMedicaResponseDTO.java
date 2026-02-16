package com.example.demo.DTOs.Response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ConsultaMedicaResponseDTO {
    private Long idConsulta;
    private Boolean estado;
    private LocalDate fechaConsulta;
    private LocalTime horaConsulta;
    private String motivo;
    private String sintomas;
    private String diagnostico;
    private String observaciones;
    private Long turnoId;
    private List<TratamientoResponseDTO> tratamientoResponseDTOList = new ArrayList<>();
}
