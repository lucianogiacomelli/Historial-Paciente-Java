package com.example.demo.Entities;

import com.example.demo.Entities.EstadoTurno.TurnoEstado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Turno")
public class Turno extends Base{
    private LocalDateTime horario;
    private String motivo;


    @ManyToOne
    private Paciente paciente;
    @ManyToOne
    private Medico medico;
    @OneToOne
    @JoinColumn(name = "consulta_id")
    private ConsultaMedica consulta;
    @OneToMany(mappedBy = "turno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TurnoEstado> estadoList = new ArrayList<>();

}
