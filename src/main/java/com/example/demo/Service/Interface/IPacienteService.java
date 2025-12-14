package com.example.demo.Service.Interface;

import com.example.demo.DTOs.Request.PacienteDTO;
import com.example.demo.DTOs.Request.UpdatePacienteDTO;
import com.example.demo.Entities.Paciente;
import org.springframework.stereotype.Service;

@Service
public interface IPacienteService {

    Paciente altaPaciente(PacienteDTO pacienteDTO) throws Exception;

    Paciente modificarPaciente(UpdatePacienteDTO pacienteDTO, Long id) throws Exception;

    void bajaPaciente(Long id) throws Exception;

    Paciente habilitarPaciente(Long id) throws Exception;

}
