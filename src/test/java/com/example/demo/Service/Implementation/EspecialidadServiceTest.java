package com.example.demo.Service.Implementation;

import com.example.demo.DTOs.Request.EspecialidadDTO;
import com.example.demo.Entities.Especialidad;
import com.example.demo.Exception.Especialidad.EspecialidadDuplicadaException;
import com.example.demo.Repository.EspecialidadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EspecialidadServiceTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private EspecialidadService especialidadService;

    // ==========================
    // CASO 1: alta OK
    // ==========================
    @Test
    void altaEspecialidad_ok() {
        // Arrange
        EspecialidadDTO dto = new EspecialidadDTO();
        dto.setNombre("Cardiología");

        when(especialidadRepository.findByNombre("Cardiología"))
                .thenReturn(Optional.empty());

        when(especialidadRepository.save(any(Especialidad.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Especialidad especialidad = especialidadService.altaEspecialidad(dto);

        // Assert
        assertNotNull(especialidad);
        assertEquals("Cardiología", especialidad.getNombre());

        verify(especialidadRepository).findByNombre("Cardiología");
        verify(especialidadRepository).save(any(Especialidad.class));
    }

    // ==========================
    // CASO 2: duplicada
    // ==========================
    @Test
    void altaEspecialidad_duplicada_lanza_excepcion() {
        // Arrange
        EspecialidadDTO dto = new EspecialidadDTO("Cardiología");

        Especialidad especialidadExistente = new Especialidad();
        especialidadExistente.setId(1L);
        especialidadExistente.setNombre("Cardiología");

        when(especialidadRepository.findByNombre("Cardiología"))
                .thenReturn(Optional.of(especialidadExistente));

        // Act + Assert
        assertThrows(
                EspecialidadDuplicadaException.class,
                () -> especialidadService.altaEspecialidad(dto)
        );
    }

}
