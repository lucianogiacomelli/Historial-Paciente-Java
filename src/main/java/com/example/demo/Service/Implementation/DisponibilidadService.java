package com.example.demo.Service.Implementation;

import com.example.demo.DTOs.Request.DisponibilidadDTO;
import com.example.demo.Entities.Dias;
import com.example.demo.Entities.DisponibilidadMedico;
import com.example.demo.Entities.Especialidad;
import com.example.demo.Entities.Medico;
import com.example.demo.Exception.Disponibilidad.DisponibilidadDuplicadaException;
import com.example.demo.Exception.Disponibilidad.DisponibilidadInvalidaException;
import com.example.demo.Exception.Disponibilidad.DisponibilidadSuperpuestaException;
import com.example.demo.Exception.Disponibilidad.DisponibilidadVaciaException;
import com.example.demo.Exception.Especialidad.EspecialidadInvalidaException;
import com.example.demo.Exception.Especialidad.EspecialidadNotFoundException;
import com.example.demo.Exception.Medico.MedicoInvalidoException;
import com.example.demo.Exception.ResourceInvalidException;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Repository.DisponibilidadRepository;
import com.example.demo.Repository.EspecialidadRepository;
import com.example.demo.Repository.MedicoRepository;
import com.example.demo.Service.Interface.IDisponibilidadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DisponibilidadService implements IDisponibilidadService {
    private DisponibilidadRepository disponibilidadRepository;
    private MedicoService medicoService;
    private MedicoRepository medicoRepository;
    private EspecialidadRepository especialidadRepository;

    public DisponibilidadService(DisponibilidadRepository disponibilidadRepository, MedicoService medicoService, EspecialidadRepository especialidadRepository, MedicoRepository medicoRepository) {
        this.medicoService = medicoService;
        this.disponibilidadRepository = disponibilidadRepository;
        this.especialidadRepository = especialidadRepository;
        this.medicoRepository = medicoRepository;
    }

    @Transactional
    @Override
    public List<DisponibilidadMedico> getDisponibilidadesByMedicoId(Long idMedico) {
        return disponibilidadRepository.findByMedicoIdAndEstadoTrue(idMedico);
    }

    @Transactional
    @Override
    public List<DisponibilidadMedico> altaDisponibilidad(List<DisponibilidadDTO> disponibilidadDTO, Long idMedico) {

        // 1. Validaciones básicas
        validarListaNoVacia(disponibilidadDTO);
        disponibilidadDTO.forEach(this::validarCamposBasicos);

        // 2. Validar duplicados y superposiciones en la lista
        validarDuplicadosEnLista(disponibilidadDTO);

        // 3. Obtener médico
        Medico medico = medicoService.getMedicoById(idMedico);

        // 4. Validar especialidades
        Set<Long> especialidadIds = disponibilidadDTO.stream()
                .map(DisponibilidadDTO::getEspecialidadId)
                .collect(Collectors.toSet());

        Map<Long, Especialidad> especialidadesMap = obtenerEspecialidades(especialidadIds);

        // 5. Validar especialidades
        disponibilidadDTO.forEach(dto ->
                validacionesEspecialidad(medico, dto, especialidadesMap)
        );

        // 6. Obtener disponibilidades existentes
        List<Dias> diasInvolucrados = disponibilidadDTO.stream()
                .map(DisponibilidadDTO::getDiaSemana)
                .distinct()
                .toList();

        List<DisponibilidadMedico> existentes =
                disponibilidadRepository.findByMedicoIdAndDiaSemana(idMedico, diasInvolucrados);

        // 7. Validar superposiciones con existentes
        for (DisponibilidadDTO dto : disponibilidadDTO) {
            List<DisponibilidadMedico> existentesMismoDia = existentes.stream()
                    .filter(e -> e.getDiaSemana().equals(dto.getDiaSemana()))
                    .toList();

            validarSuperposicionConExistentes(dto, existentesMismoDia);
        }

        // 8. Crear nuevas disponibilidades
        List<DisponibilidadMedico> nuevasDisponibilidades = disponibilidadDTO.stream()
                .map(dto -> crearDisponibilidad(dto, medico, especialidadesMap))
                .toList();

        return disponibilidadRepository.saveAll(nuevasDisponibilidades);
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
        disponibilidadRepository.save(disponibilidad);
    }


    @Transactional
    @Override
    public DisponibilidadMedico modificarDisponibilidad(DisponibilidadDTO disponibilidadDTO, Long idMedico, Long idDisponibilidad ){
        Optional<DisponibilidadMedico> disponibilidadMedicoOpt = disponibilidadRepository.findById(idDisponibilidad);
        if(disponibilidadMedicoOpt.isEmpty()){
            throw new ResourceNotFoundException("No se ha encontrado la disponibilidad que ha querido modificar.");
        }
        DisponibilidadMedico disponibilidadMedico = disponibilidadMedicoOpt.get();
        if(!disponibilidadMedico.getEstado()){
            throw new DisponibilidadInvalidaException("La disponibilidad seleccionada se encuentra dada de baja.");
        }

        validarCamposBasicos(disponibilidadDTO);

        Medico medico = medicoService.getMedicoById(idMedico);

        Map<Long, Especialidad> especialidadMap = obtenerEspecialidades(Set.of(disponibilidadDTO.getEspecialidadId()));

        validacionesEspecialidad(medico, disponibilidadDTO, especialidadMap);

        List<DisponibilidadMedico> existentes =
                disponibilidadRepository.findByMedicoIdAndDiaSemana(
                                idMedico,
                                List.of(disponibilidadDTO.getDiaSemana())
                        ).stream()
                        .filter(d -> !d.getId().equals(idDisponibilidad))
                        .toList();

        validarSuperposicionConExistentes(disponibilidadDTO, existentes);

        disponibilidadMedico.setDiaSemana(disponibilidadDTO.getDiaSemana());
        disponibilidadMedico.setHoraInicio(disponibilidadDTO.getHoraInicio());
        disponibilidadMedico.setHoraFin(disponibilidadDTO.getHoraFin());
        disponibilidadMedico.setEspecialidad(
                especialidadMap.get(disponibilidadDTO.getEspecialidadId())
        );

        return disponibilidadRepository.save(disponibilidadMedico);

    }

    // --------------------------------
    // MÉTODOS AUXILIARES DE CREACIÓN
    // --------------------------------

    private DisponibilidadMedico crearDisponibilidad(
            DisponibilidadDTO dto,
            Medico medico,
            Map<Long, Especialidad> especialidadesMap) {

        DisponibilidadMedico disponibilidad = new DisponibilidadMedico();
        disponibilidad.setDiaSemana(dto.getDiaSemana());
        disponibilidad.setHoraInicio(dto.getHoraInicio());
        disponibilidad.setHoraFin(dto.getHoraFin());
        disponibilidad.setMedico(medico);
        disponibilidad.setEspecialidad(especialidadesMap.get(dto.getEspecialidadId()));

        return disponibilidad;
    }

    private Map<Long, Especialidad> obtenerEspecialidades(Set<Long> especialidadIds) {
        List<Especialidad> especialidades = especialidadRepository.findAllById(especialidadIds);

        if (especialidades.size() != especialidadIds.size()) {
            Set<Long> encontradas = especialidades.stream()
                    .map(Especialidad::getId)
                    .collect(Collectors.toSet());
            Set<Long> faltantes = new HashSet<>(especialidadIds);
            faltantes.removeAll(encontradas);

            throw new EspecialidadNotFoundException(
                    "El/Los siguiente/s ID/s de especialidad/es no existen: " + faltantes
            );
        }

        Map<Long, Especialidad> especialidadesMap = new HashMap<>();
        for (Especialidad esp : especialidades) {
            if (Boolean.FALSE.equals(esp.getEstado())) {
                throw new EspecialidadInvalidaException(
                        "La especialidad con id " + esp.getId() + " se encuentra dada de baja"
                );
            }
            especialidadesMap.put(esp.getId(), esp);
        }

        return especialidadesMap;
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

    public void validacionesEspecialidad(
            Medico medico,
            DisponibilidadDTO disponibilidadDTO,
            Map<Long, Especialidad> especialidadesMap) {

        Especialidad especialidad = especialidadesMap.get(disponibilidadDTO.getEspecialidadId());

        if (!medico.getEspecialidades().contains(especialidad)) {
            throw new ResourceInvalidException(
                    "El médico no tiene asignada la especialidad: " + especialidad.getNombre()
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
