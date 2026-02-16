package com.example.demo.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Consulta")
public class ConsultaMedica extends Base {
    private LocalDate fechaConsulta;
    private LocalTime horaConsulta;
    private String motivo;
    private String sintomas;
    private String diagnostico;
    private String observaciones;
    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tratamiento> tratamientoList = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;



}
