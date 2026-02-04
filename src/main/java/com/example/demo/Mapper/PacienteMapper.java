package com.example.demo.Mapper;
import com.example.demo.DTOs.Response.PacienteResponseDTO;
import com.example.demo.Entities.Paciente;

public class PacienteMapper {
    public static PacienteResponseDTO toDTO(Paciente paciente) {
        return new PacienteResponseDTO(
                paciente.getId(),
                paciente.getEstado(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getDni(),
                paciente.getFechaNacimiento(),
                paciente.getGenero(),
                paciente.getNumero(),
                paciente.getEmail(),
                paciente.getDireccion()
        );
    }
}
