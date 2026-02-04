package com.example.demo.Service.Interface;

import com.example.demo.DTOs.Request.PacienteDTO;
import com.example.demo.DTOs.Request.UpdatePacienteDTO;
import com.example.demo.Entities.Paciente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPacienteService {

    Paciente altaPaciente(PacienteDTO pacienteDTO) ;

    Paciente modificarPaciente(UpdatePacienteDTO pacienteDTO, Long id);

    void bajaPaciente(Long id) ;

    Paciente habilitarPaciente(Long id) ;

    List<Paciente> getAllPacientes();

    List<Paciente> getPacientesActivos();

    Paciente getPacienteById(Long id);

}
