package com.example.demo.Repository;

import com.example.demo.Entities.Medico;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicoRepository extends BaseRepository<Medico, Long> {
    @Query("Select m FROM Medico m WHERE m.matricula=:matricula AND m.dni=:dni")
    Optional<Medico> findByMatriculaAndDni(@Param("matricula")String matricula, @Param("dni") String dni);

}
