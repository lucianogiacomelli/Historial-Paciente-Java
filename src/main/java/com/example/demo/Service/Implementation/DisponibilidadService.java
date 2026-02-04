package com.example.demo.Service.Implementation;

import com.example.demo.DTOs.Request.DisponibilidadDTO;
import com.example.demo.Entities.Dias;
import com.example.demo.Entities.DisponibilidadMedico;
import com.example.demo.Entities.Medico;
import com.example.demo.Exception.Disponibilidad.DisponibilidadDuplicadaException;
import com.example.demo.Exception.Disponibilidad.DisponibilidadInvalidaException;
import com.example.demo.Exception.Disponibilidad.DisponibilidadSuperpuestaException;
import com.example.demo.Exception.Disponibilidad.DisponibilidadVaciaException;
import com.example.demo.Repository.DisponibilidadRepository;
import com.example.demo.Service.Interface.IDisponibilidadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DisponibilidadService implements IDisponibilidadService {
    private DisponibilidadRepository disponibilidadRepository;
    private MedicoService medicoService;

    public DisponibilidadService(DisponibilidadRepository disponibilidadRepository, MedicoService medicoService) {
        this.medicoService = medicoService;
        this.disponibilidadRepository = disponibilidadRepository;
    }

    @Transactional
    @Override
    public List<DisponibilidadMedico> getDisponibilidadesByMedicoId(Long idMedico) {
        return disponibilidadRepository.findByMedicoIdAndEstadoTrue(idMedico);
    }

    @Transactional
    @Override
    public List<DisponibilidadMedico> altaDisponibilidad(
            List<DisponibilidadDTO> disponibilidadDTO,
            Long idMedico) {

        // 1. Validaciones básicas
        validarListaNoVacia(disponibilidadDTO);
        disponibilidadDTO.forEach(this::validarCamposBasicos);

        // 2. Validar duplicados y superposiciones en la lista
        validarDuplicadosEnLista(disponibilidadDTO);

        // 3. Obtener médico
        Medico medico = medicoService.getMedicoById(idMedico);

        // 4. Obtener disponibilidades existentes (UNA SOLA QUERY)
        List<Dias> diasInvolucrados = disponibilidadDTO.stream()
                .map(DisponibilidadDTO::getDiaSemana)
                .distinct()
                .toList();

        List<DisponibilidadMedico> existentes =
                disponibilidadRepository.findByMedicoIdAndDiaSemana(idMedico, diasInvolucrados);

        // 5. Validar superposiciones con existentes
        for (DisponibilidadDTO dto : disponibilidadDTO) {
            List<DisponibilidadMedico> existentesMismoDia = existentes.stream()
                    .filter(e -> e.getDiaSemana().equals(dto.getDiaSemana()))
                    .toList();

            validarSuperposicionConExistentes(dto, existentesMismoDia);
        }

        // 6. Crear y guardar nuevas disponibilidades
        List<DisponibilidadMedico> nuevasDisponibilidades = disponibilidadDTO.stream()
                .map(dto -> crearDisponibilidad(dto, medico))
                .toList();

        return disponibilidadRepository.saveAll(nuevasDisponibilidades);
    }



    private DisponibilidadMedico crearDisponibilidad(DisponibilidadDTO dto, Medico medico) {
        DisponibilidadMedico disponibilidad = new DisponibilidadMedico();
        disponibilidad.setDiaSemana(dto.getDiaSemana());
        disponibilidad.setHoraInicio(dto.getHoraInicio());
        disponibilidad.setHoraFin(dto.getHoraFin());
        disponibilidad.setMedico(medico);
        return disponibilidad;
    }


    @Transactional
    @Override
    public void bajaDisponibilidad(Long idDisponibilidad, Long idMedico) {
        DisponibilidadMedico disponibilidad = disponibilidadRepository
                .findByIdAndMedicoId(idDisponibilidad, idMedico)
                .orElseThrow(() ->
                        new DisponibilidadInvalidaException(
                                "La disponibilidad no existe o no pertenece al médico con id: " + idMedico
                        )
                );
        disponibilidad.setFechaBaja(LocalDateTime.now());
        disponibilidad.setEstado(false);
    }


    // --------------------------------
    // MÉTODOS AUXILIARES DE VALIDACIÓN
    // --------------------------------

    public void validarListaNoVacia(List<DisponibilidadDTO> disponibilidades) {
        if (disponibilidades == null || disponibilidades.isEmpty()) {
            throw new DisponibilidadVaciaException(
                    "La lista de disponibilidades no puede ser nula o vacía"
            );
        }
    }

    public void validarCamposBasicos(DisponibilidadDTO dto) {
        if (dto.getDiaSemana() == null) {
            throw new DisponibilidadInvalidaException(
                    "El día de la semana no puede ser nulo"
            );
        }

        if (dto.getHoraInicio() == null || dto.getHoraFin() == null) {
            throw new DisponibilidadInvalidaException(
                    "Las horas no pueden ser nulas"
            );
        }

        if (!dto.getHoraInicio().isBefore(dto.getHoraFin())) {
            throw new DisponibilidadInvalidaException(
                    "La hora de inicio debe ser anterior a la hora de fin"
            );
        }
    }

    public void validarDuplicadosEnLista(List<DisponibilidadDTO> disponibilidades) {
        for (int i = 0; i < disponibilidades.size(); i++) {
            for (int j = i + 1; j < disponibilidades.size(); j++) {
                DisponibilidadDTO a = disponibilidades.get(i);
                DisponibilidadDTO b = disponibilidades.get(j);

                if (sonDuplicadas(a, b)) {
                    throw new DisponibilidadDuplicadaException(
                            String.format("Hay disponibilidades duplicadas para el día %s",
                                    a.getDiaSemana())
                    );
                }

                if (seSuperponen(a, b)) {
                    throw new DisponibilidadSuperpuestaException(
                            String.format("Las disponibilidades N° %d y %d del día %s se superponen entre sí",
                                    i, j, a.getDiaSemana())
                    );
                }
            }
        }
    }

    public void validarSuperposicionConExistentes(
            DisponibilidadDTO dto,
            List<DisponibilidadMedico> existentes) {

        for (DisponibilidadMedico existente : existentes) {
            if (superpone(dto, existente)) {
                throw new DisponibilidadSuperpuestaException(
                        String.format(
                                "La disponibilidad del día %s (%s - %s) se superpone con una existente (%s - %s)",
                                dto.getDiaSemana(),
                                dto.getHoraInicio(),
                                dto.getHoraFin(),
                                existente.getHoraInicio(),
                                existente.getHoraFin()
                        )
                );
            }
        }
    }

    private boolean sonDuplicadas(DisponibilidadDTO a, DisponibilidadDTO b) {
        return a.getDiaSemana().equals(b.getDiaSemana()) &&
                a.getHoraInicio().equals(b.getHoraInicio()) &&
                a.getHoraFin().equals(b.getHoraFin());
    }

    private boolean seSuperponen(DisponibilidadDTO a, DisponibilidadDTO b) {
        return a.getDiaSemana().equals(b.getDiaSemana()) &&
                a.getHoraInicio().isBefore(b.getHoraFin()) &&
                b.getHoraInicio().isBefore(a.getHoraFin());
    }

    private boolean superpone(DisponibilidadDTO dto, DisponibilidadMedico existente) {
        return dto.getHoraInicio().isBefore(existente.getHoraFin()) &&
                existente.getHoraInicio().isBefore(dto.getHoraFin());
    }

}
