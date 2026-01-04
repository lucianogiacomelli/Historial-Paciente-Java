package com.example.demo.Service.Implementation;

import com.example.demo.DTOs.Request.DisponibilidadDTO;
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
    public List<DisponibilidadMedico> altaDisponibilidad(List<DisponibilidadDTO> disponibilidadDTO, Long idMedico) {
        if (disponibilidadDTO == null || disponibilidadDTO.isEmpty()) {
            throw new DisponibilidadVaciaException("La lista de disponibilidades no puede ser nula o vacía");
        }
        for (DisponibilidadDTO dto : disponibilidadDTO) {
            if (dto.getDiaSemana() == null) {
                throw new DisponibilidadInvalidaException("El día de la semana no puede ser nulo");
            }

            if (dto.getHoraInicio() == null || dto.getHoraFin() == null) {
                throw new DisponibilidadInvalidaException("Las horas no pueden ser nulas");
            }

            if (!dto.getHoraInicio().isBefore(dto.getHoraFin())) {
                throw new DisponibilidadInvalidaException(
                        "La hora de inicio debe ser anterior a la hora de fin"
                );
            }
        }


        Medico medico = medicoService.getMedicoById(idMedico);
        for (int i = 0; i < disponibilidadDTO.size(); i++) {
            for (int j = i + 1; j < disponibilidadDTO.size(); j++) {
                DisponibilidadDTO a = disponibilidadDTO.get(i);
                DisponibilidadDTO b = disponibilidadDTO.get(j);
                if (a.getDiaSemana().equals(b.getDiaSemana()) &&
                        a.getHoraInicio().equals(b.getHoraInicio()) &&
                        a.getHoraFin().equals(b.getHoraFin())) {

                    throw new DisponibilidadDuplicadaException(
                            "Hay disponibilidades duplicadas para el día " + a.getDiaSemana()
                    );
                }
                if (a.getDiaSemana().equals(b.getDiaSemana())
                        && a.getHoraInicio().isBefore(b.getHoraFin())
                        && b.getHoraInicio().isBefore(a.getHoraFin())) {

                    throw new DisponibilidadSuperpuestaException(
                            "Las disponibilidades N° " + (i) + " y " + (j) +
                                    " del día " + a.getDiaSemana() +
                                    " se superponen entre sí"
                    );
                }
            }
        }
        for (DisponibilidadDTO dto : disponibilidadDTO) {

            List<DisponibilidadMedico> existentes = disponibilidadRepository.findByMedicoIdAndDiaSemana(idMedico, dto.getDiaSemana());
            for (DisponibilidadMedico existente : existentes) {
                if (superpone(dto, existente)) {
                    throw new DisponibilidadSuperpuestaException("La disponibilidad que se quiere cargar en el día " + dto.getDiaSemana() +  ", en el horario de inicio: " + dto.getHoraInicio()+
                            " y de fin: " + dto.getHoraFin()+ "se superpone con una existente: " + existente.getHoraInicio() + " - " + existente.getHoraFin());
                }
            }
        }
        List <DisponibilidadMedico> nuevasDisponibilidades = new ArrayList<>();
        for (DisponibilidadDTO dto : disponibilidadDTO) {
            DisponibilidadMedico nuevaDisponibilidad = new DisponibilidadMedico();
            nuevaDisponibilidad.setDiaSemana(dto.getDiaSemana());
            nuevaDisponibilidad.setHoraInicio(dto.getHoraInicio());
            nuevaDisponibilidad.setHoraFin(dto.getHoraFin());
            nuevaDisponibilidad.setMedico(medico);
            nuevasDisponibilidades.add(nuevaDisponibilidad);
        }
        disponibilidadRepository.saveAll(nuevasDisponibilidades);
        return nuevasDisponibilidades;
    }



    private boolean superpone (DisponibilidadDTO dto, DisponibilidadMedico existente) {
        return dto.getHoraInicio().isBefore(existente.getHoraFin()) &&
                existente.getHoraInicio().isBefore(dto.getHoraFin());
    }

}
