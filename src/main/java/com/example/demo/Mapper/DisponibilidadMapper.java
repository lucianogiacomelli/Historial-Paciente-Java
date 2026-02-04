package com.example.demo.Mapper;

import com.example.demo.DTOs.Response.DisponibilidadResponseDTO;
import com.example.demo.Entities.DisponibilidadMedico;

import java.util.ArrayList;
import java.util.List;

public class DisponibilidadMapper {
    public static List<DisponibilidadResponseDTO> toDTO(List<DisponibilidadMedico> disponibilidad) {

        return disponibilidad.stream()
                .map(
                disponibilidadMedico -> DisponibilidadResponseDTO.builder()
                        .id(disponibilidadMedico.getId())
                        .diaSemana(disponibilidadMedico.getDiaSemana())
                        .horaInicio(disponibilidadMedico.getHoraInicio())
                        .horaFin(disponibilidadMedico.getHoraFin())
                        .medicoId(disponibilidadMedico.getMedico().getId())
                        .build()
                ).toList();

    }
}
