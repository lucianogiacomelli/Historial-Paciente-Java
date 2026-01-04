package com.example.demo.Service.Implementation;

import com.example.demo.DTOs.Request.EspecialidadDTO;
import com.example.demo.Entities.Especialidad;
import com.example.demo.Exception.Especialidad.EspecialidadDuplicadaException;
import com.example.demo.Exception.Especialidad.EspecialidadInvalidaException;
import com.example.demo.Repository.EspecialidadRepository;
import com.example.demo.Service.Interface.IEspecialidadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EspecialidadService implements IEspecialidadService {

    private EspecialidadRepository especialidadRepository;

    public EspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    @Transactional
    @Override
    public Especialidad altaEspecialidad(EspecialidadDTO especialidadDTO) {

        especialidadRepository.findByNombre(especialidadDTO.getNombre())
                .ifPresent(e -> { throw new EspecialidadDuplicadaException("La especialidad ya se encuentra registrada"); });

        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(especialidadDTO.getNombre());

        return especialidadRepository.save(especialidad);
    }

}
