package com.example.demo.Service.Interface;

import com.example.demo.DTOs.Request.DisponibilidadDTO;
import com.example.demo.Entities.DisponibilidadMedico;
import com.example.demo.Entities.Turno;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IDisponibilidadService {
    List<DisponibilidadMedico> altaDisponibilidad(List<DisponibilidadDTO> disponibilidadDTO, Long idMedico);

    DisponibilidadMedico modificarDisponibilidad(DisponibilidadDTO disponibilidadDTO, Long idMedico, Long idDisponibilidad);

    List<DisponibilidadMedico> getDisponibilidadesByMedicoId(Long idMedico);

    void bajaDisponibilidad(Long idDisponibilidad, Long idMedico);
}
