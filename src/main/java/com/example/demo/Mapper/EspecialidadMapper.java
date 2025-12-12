package com.example.demo.Mapper;


import com.example.demo.DTOs.Response.EspecialidadResponseDTO;
import com.example.demo.Entities.Especialidad;

public class EspecialidadMapper {
    public static EspecialidadResponseDTO toDTO(Especialidad especialidad) {
        return new EspecialidadResponseDTO(
                especialidad.getId(),
                especialidad.getNombre()
        );
    }
}
