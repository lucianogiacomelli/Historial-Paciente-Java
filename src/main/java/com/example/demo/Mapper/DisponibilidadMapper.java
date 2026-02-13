package com.example.demo.Mapper;

import com.example.demo.DTOs.Response.DisponibilidadResponseDTO;
import com.example.demo.Entities.DisponibilidadMedico;

import java.util.ArrayList;
import java.util.List;

public class DisponibilidadMapper {
    public static DisponibilidadResponseDTO toDTO(DisponibilidadMedico disponibilidadMedico) {
        return new DisponibilidadResponseDTO(
                disponibilidadMedico.getId(),
                disponibilidadMedico.getDiaSemana(),
                disponibilidadMedico.getHoraInicio(),
                disponibilidadMedico.getHoraFin(),
                disponibilidadMedico.getMedico().getId(),
                disponibilidadMedico.getMedico().getNombre()+" "+disponibilidadMedico.getMedico().getApellido(),
                disponibilidadMedico.getEspecialidad().getId(),
                disponibilidadMedico.getEspecialidad().getNombre(),
                disponibilidadMedico.getEspecialidad().getDuracionConsulta()
        );
    }
}
