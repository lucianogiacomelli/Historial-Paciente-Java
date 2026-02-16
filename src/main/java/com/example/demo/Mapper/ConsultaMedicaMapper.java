package com.example.demo.Mapper;

import com.example.demo.DTOs.Response.ConsultaMedicaResponseDTO;
import com.example.demo.DTOs.Response.TratamientoResponseDTO;
import com.example.demo.Entities.ConsultaMedica;
import com.example.demo.Entities.Tratamiento;

import java.util.List;


public class ConsultaMedicaMapper {
    public static ConsultaMedicaResponseDTO toDTO(ConsultaMedica consultaMedica){
        List <TratamientoResponseDTO> tratamientoResponseDTOList = consultaMedica.getTratamientoList()
                .stream().map(ConsultaMedicaMapper::TratamientoToDto).toList();
        return new ConsultaMedicaResponseDTO(
                consultaMedica.getId(),
                consultaMedica.getEstado(),
                consultaMedica.getFechaConsulta(),
                consultaMedica.getHoraConsulta(),
                consultaMedica.getMotivo(),
                consultaMedica.getSintomas(),
                consultaMedica.getDiagnostico(),
                consultaMedica.getObservaciones(),
                consultaMedica.getTurno().getId(),
                tratamientoResponseDTOList

        );
    }

    private static TratamientoResponseDTO TratamientoToDto(Tratamiento tratamiento){
        return new TratamientoResponseDTO(
                tratamiento.getDescripcion(),
                tratamiento.getDosis(),
                tratamiento.getFrecuencia(),
                tratamiento.getDuracionDias(),
                tratamiento.getIndicaciones()
        );
    }
}
