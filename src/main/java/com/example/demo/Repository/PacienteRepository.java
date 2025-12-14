package com.example.demo.Repository;

import com.example.demo.Entities.Paciente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PacienteRepository extends BaseRepository<Paciente, Long>{
    @Query("Select p FROM Paciente p WHERE p.dni=:dni")
    Optional<Paciente> findByDni(@Param("dni") String dni);
}
