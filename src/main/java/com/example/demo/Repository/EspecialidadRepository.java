package com.example.demo.Repository;

import com.example.demo.Entities.Especialidad;

import java.util.List;
import java.util.Optional;

public interface EspecialidadRepository extends BaseRepository<Especialidad, Long> {
    Optional<Especialidad> findAllById(Long id);
    Optional<Especialidad> findByNombre(String nombre);
}
