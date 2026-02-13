package com.example.demo.Entities;

import com.example.demo.Entities.EstadoTurno.EstadoTurno;
import com.example.demo.Entities.EstadoTurno.TurnoEstado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Turno")
public class Turno extends Base{
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private LocalDate fecha;

    @ManyToOne
    private Paciente paciente;
    @ManyToOne
    private Medico medico;
    @OneToOne
    @JoinColumn(name = "consulta_id")
    private ConsultaMedica consulta;
    @OrderBy("fecha ASC")
    @OneToMany(mappedBy = "turno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TurnoEstado> estadoList = new ArrayList<>();
    @ManyToOne
    private Especialidad especialidad;

    @Enumerated
    private EstadoTurno estadoTurno;


    public TurnoEstado getUltimoEstado() {
        if (estadoList.isEmpty()) {
            return null;
        }
        return estadoList.get(estadoList.size() - 1);
    }

    public EstadoTurno getEstadoActual() {
        TurnoEstado ultimo = getUltimoEstado();
        return ultimo != null ? ultimo.getEstadoTurno() : null;
    }


}
