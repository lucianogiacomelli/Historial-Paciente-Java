package com.example.demo.Mapper;

import com.example.demo.DTOs.Response.MedicoResponseDTO;
import com.example.demo.Entities.Especialidad;
import com.example.demo.Entities.Medico;

public class MedicoMapper {
    public static MedicoResponseDTO toDTO(Medico medico) {
        return new MedicoResponseDTO(
                medico.getId(),
                medico.getNombre(),
                medico.getApellido(),
                medico.getEmail(),
                medico.getMatricula(),
                medico.getEspecialidades()
                        .stream()
                        .map(Especialidad::getNombre)
                        .toList()
        );
    }
}
