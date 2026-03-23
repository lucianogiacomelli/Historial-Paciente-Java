package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "disponibilidad_medico")
public class DisponibilidadMedico extends Base{

    @ManyToOne
    private Medico medico;

    @Enumerated(EnumType.STRING)
    private Dias diaSemana;

    private LocalTime horaInicio;
    private LocalTime horaFin;

    @ManyToOne
    private Especialidad especialidad;

}
