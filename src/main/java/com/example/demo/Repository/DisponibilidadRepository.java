package com.example.demo.Repository;

import com.example.demo.Entities.Dias;
import com.example.demo.Entities.DisponibilidadMedico;

import java.util.List;

public interface DisponibilidadRepository extends BaseRepository<DisponibilidadMedico, Long> {
    List<DisponibilidadMedico> findByMedicoIdAndDiaSemana(
            Long idMedico,
            Dias diaSemana
    );
}
