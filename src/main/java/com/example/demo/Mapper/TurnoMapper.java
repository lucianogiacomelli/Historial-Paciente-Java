package com.example.demo.Mapper;

import com.example.demo.DTOs.Response.TurnoEstadoResponseDTO;
import com.example.demo.DTOs.Response.TurnoResponseDTO;
import com.example.demo.Entities.EstadoTurno.TurnoEstado;
import com.example.demo.Entities.Turno;

import java.util.List;

public class TurnoMapper {
    public static TurnoResponseDTO toResponse(Turno turno) {
        TurnoResponseDTO dto = new TurnoResponseDTO();

        dto.setId(turno.getId());
        dto.setFecha(turno.getFecha());
        dto.setHoraInicio(turno.getHorarioInicio());
        dto.setHoraFin(turno.getHorarioFin());
        dto.setEstado(turno.getEstadoActual());

        dto.setMedicoId(turno.getMedico().getId());
        dto.setMedicoNombreCompleto(
                turno.getMedico().getNombre() + " " + turno.getMedico().getApellido()
        );

        dto.setPacienteId(turno.getPaciente().getId());
        dto.setPacienteNombreCompleto(
                turno.getPaciente().getNombre() + " " + turno.getPaciente().getApellido()
        );

        dto.setEspecialidadId(turno.getEspecialidad().getId());
        dto.setEspecialidadNombre(turno.getEspecialidad().getNombre());

        List<TurnoEstadoResponseDTO> historial = turno.getEstadoList()
                .stream()
                .map(TurnoMapper::mapEstado)
                .toList();

        dto.setHistorialEstados(historial);

        return dto;
    }

    private static TurnoEstadoResponseDTO mapEstado(TurnoEstado estado) {
        TurnoEstadoResponseDTO dto = new TurnoEstadoResponseDTO();
        dto.setEstado(estado.getEstadoTurno());
        dto.setFecha(estado.getFecha());
        return dto;
    }
}
