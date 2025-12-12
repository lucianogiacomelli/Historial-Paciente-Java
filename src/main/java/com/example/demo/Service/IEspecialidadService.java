package com.example.demo.Service;

import com.example.demo.DTOs.Request.EspecialidadDTO;
import com.example.demo.Entities.Especialidad;
import org.springframework.stereotype.Service;

@Service
public interface IEspecialidadService {
    Especialidad altaEspecialidad(EspecialidadDTO especialidadDTO) throws Exception;
}
