package com.example.demo.Repository;

import com.example.demo.Entities.Turno;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TurnoRepository extends BaseRepository<Turno, Long> {
    @Query("""
    SELECT COUNT(t) > 0
    FROM Turno t
    WHERE t.medico.id = :medicoId
      AND t.fecha = :fecha
      AND (
            :horaInicio < t.horarioFin
        AND :horaFin > t.horarioInicio
      )
""")
    boolean existeTurnoSolapado(
            @Param("medicoId") Long medicoId,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin
    );


    //Optional<Turno> findByMedicoIdAndPacienteIdAndHorario(Long medicoId, Long pacienteId, LocalDateTime horario);

    List<Turno> getAllTurnosByEstadoTrue();
}
