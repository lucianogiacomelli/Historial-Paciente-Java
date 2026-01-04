package com.example.demo.Service.Interface;

import com.example.demo.DTOs.Request.DisponibilidadDTO;
import com.example.demo.Entities.DisponibilidadMedico;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IDisponibilidadService {
    List<DisponibilidadMedico> altaDisponibilidad(List<DisponibilidadDTO> disponibilidadDTO, Long idMedico);
}
