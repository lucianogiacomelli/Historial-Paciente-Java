package com.example.demo.Repository;

import com.example.demo.Entities.Turno;


import java.time.LocalDateTime;
import java.util.Optional;

public interface TurnoRepository extends BaseRepository<Turno, Long> {
    Optional<Turno> findByMedicoIdAndPacienteIdAndHorario(Long medicoId, Long pacienteId, LocalDateTime horario);
}
