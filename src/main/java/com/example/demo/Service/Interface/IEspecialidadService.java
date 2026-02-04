package com.example.demo.Service.Interface;

import com.example.demo.DTOs.Request.EspecialidadDTO;
import com.example.demo.Entities.Especialidad;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IEspecialidadService {
    Especialidad altaEspecialidad(EspecialidadDTO especialidadDTO);

    void bajaEspecialidad(Long idEspecialidad);

    List<Especialidad> getEspecialidadesActivas();

    List<Especialidad> getAllEspecialidades();

    Especialidad altaEspecialidadDadaDeBaja(Long idEspecialidad);
}
