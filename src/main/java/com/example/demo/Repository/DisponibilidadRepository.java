package com.example.demo.Repository;

import com.example.demo.Entities.Dias;
import com.example.demo.Entities.DisponibilidadMedico;
import com.example.demo.Entities.Medico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DisponibilidadRepository extends BaseRepository<DisponibilidadMedico, Long> {


    @Query("SELECT d FROM DisponibilidadMedico d WHERE d.medico.id = :medicoId AND d.diaSemana IN :dias")
    List<DisponibilidadMedico> findByMedicoIdAndDiaSemana(
            @Param("medicoId") Long medicoId,
            @Param("dias") List<Dias> dias
    );

    List <DisponibilidadMedico> findByMedicoIdAndEstadoTrue(Long idMedico);

    Optional<DisponibilidadMedico> findByIdAndMedicoId(Long idDisponibilidad, Long idMedico);

    Optional<DisponibilidadMedico> findByMedicoAndDiaSemana(Medico medico, Dias diaSemana);
}
