package com.example.demo.Service.Interface;

import com.example.demo.Entities.ConsultaMedica;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IConsultaMedicaService {
    List<ConsultaMedica> getHistorialPaciente (Long pacienteId);
    ConsultaMedica getConsulta (Long idConsulta);
}
