package com.example.demo.Service.Implementation;

import com.example.demo.DTOs.Request.EspecialidadDTO;
import com.example.demo.Entities.Especialidad;
import com.example.demo.Exception.Especialidad.EspecialidadDuplicadaException;
import com.example.demo.Exception.Especialidad.EspecialidadInvalidaException;
import com.example.demo.Exception.Especialidad.EspecialidadNotFoundException;
import com.example.demo.Repository.EspecialidadRepository;
import com.example.demo.Service.Interface.IEspecialidadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
                .ifPresent(e -> { throw new EspecialidadDuplicadaException("La especialidad se encuentra duplicada o dada de baja"); });

        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(especialidadDTO.getNombre());
        especialidad.setDuracionConsulta(especialidadDTO.getDuracionTurno());

        return especialidadRepository.save(especialidad);
    }

    @Transactional
    @Override
    public Especialidad modificarEspecialidad(Long idEspecialidad, EspecialidadDTO especialidadDTO) {
        Optional <Especialidad> especialidadOptional = especialidadRepository.findById(idEspecialidad);
        if (especialidadOptional.isEmpty()){
            throw new EspecialidadNotFoundException("La especialidad con id: "+ idEspecialidad +" no se ha encontrado.");
        }
        Especialidad especialidad = especialidadOptional.get();

        if(especialidad.getEstado() == false){
            throw new EspecialidadInvalidaException("La especialidad con id: "+idEspecialidad+" se encuentra dada de baja.");
        }

        if (especialidadDTO.getDuracionTurno() != null){
            especialidad.setDuracionConsulta(especialidadDTO.getDuracionTurno());
        }
        if(especialidadDTO.getNombre() != null){
            especialidad.setNombre(especialidadDTO.getNombre());
        }
        return especialidadRepository.save(especialidad);

    }

    @Transactional
    @Override
    public Especialidad altaEspecialidadDadaDeBaja(Long idEspecialiadad) {

        Optional <Especialidad> especialidadOptional = especialidadRepository.findById(idEspecialiadad);

        if(especialidadOptional.isEmpty()) {
            throw new EspecialidadNotFoundException("La especialidad con id " + idEspecialiadad + " no existe.");
        }

        Especialidad especialidad = especialidadOptional.get();

        if(especialidadOptional.get().getEstado()){
            throw new EspecialidadInvalidaException("La especialidad con id " + idEspecialiadad + " ya se encuentra activa.");
        }

        especialidad.setEstado(true);
        especialidad.setFechaBaja(null);

        return especialidadRepository.save(especialidad);
    }

    @Transactional
    @Override
    public void bajaEspecialidad(Long idEspecialidad) {
        Optional <Especialidad> especialidadOptional = especialidadRepository.findById(idEspecialidad);
        if(especialidadOptional.isEmpty()) {
            throw new EspecialidadInvalidaException("La especialidad con id " + idEspecialidad + " no existe.");
        }
        Especialidad especialidad = especialidadOptional.get();

        especialidad.setEstado(false);
        especialidad.setFechaBaja(LocalDateTime.now());

    }

    @Override
    public List<Especialidad> getEspecialidadesActivas() {
        List <Especialidad> especialidades = especialidadRepository.findByEstadoTrue();

        return especialidades;
    }

    @Override
    public List<Especialidad> getAllEspecialidades() {
        List <Especialidad> especialidades = especialidadRepository.findAll();

        return especialidades;
    }



}
