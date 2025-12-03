package com.example.demo.Entities.EstadoTurno;

import com.example.demo.Entities.Base;
import com.example.demo.Entities.Turno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TurnoEstado")
public class TurnoEstado extends Base {
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private EstadoTurno estadoTurno;

    @ManyToOne
    @JoinColumn(name = "turno_id")
    private Turno turno;
}
