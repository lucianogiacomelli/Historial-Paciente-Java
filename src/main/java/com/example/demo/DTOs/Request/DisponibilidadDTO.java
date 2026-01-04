package com.example.demo.DTOs.Request;


import com.example.demo.Entities.Dias;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DisponibilidadDTO {

    @Enumerated(EnumType.STRING)
    private Dias diaSemana;

    private LocalTime horaInicio;
    private LocalTime horaFin;

}
