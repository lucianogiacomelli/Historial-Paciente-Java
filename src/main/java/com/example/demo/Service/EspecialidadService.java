package com.example.demo.Service;

import com.example.demo.DTOs.Request.EspecialidadDTO;
import com.example.demo.Entities.Especialidad;
import com.example.demo.Repository.EspecialidadRepository;
import org.springframework.stereotype.Service;

@Service
public class EspecialidadService implements IEspecialidadService {

    private EspecialidadRepository especialidadRepository;

    public EspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Override
    public Especialidad altaEspecialidad(EspecialidadDTO especialidadDTO) throws Exception {

        especialidadRepository.findByNombre(especialidadDTO.getNombre())
                .ifPresent(e -> { throw new RuntimeException("La especialidad ya se encuentra registrada"); });

        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(especialidadDTO.getNombre());

        return especialidadRepository.save(especialidad);
    }

}
