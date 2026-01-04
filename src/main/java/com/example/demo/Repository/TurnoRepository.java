package com.example.demo.Repository;

import com.example.demo.Entities.Turno;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TurnoRepository extends BaseRepository<Turno, Long> {
    @Query()
    Optional<Turno> findTurnoExistente(@Param("id-paciente") Long idPaciente, @Param("fecha") LocalDateTime horario);
}
