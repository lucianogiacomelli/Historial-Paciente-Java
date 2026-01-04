package com.example.demo.Service.Implementation;

import com.example.demo.Configuration.Auth0.Auth0Properties;
import com.example.demo.DTOs.Request.MedicoDTO;
import com.example.demo.Entities.Especialidad;
import com.example.demo.Entities.Medico;
import com.example.demo.Exception.Especialidad.EspecialidadNotFoundException;
import com.example.demo.Exception.Medico.MedicoDuplicadoException;
import com.example.demo.Exception.Medico.MedicoNotFoundException;
import com.example.demo.Repository.EspecialidadRepository;
import com.example.demo.Repository.MedicoRepository;
import com.example.demo.Service.auth0.Auth0Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private EspecialidadRepository especialidadRepository;

    @Mock
    private Auth0Service auth0Service;

    @Mock
    private Auth0Properties auth0Properties;

    @InjectMocks
    private MedicoService medicoService;



    @Test
    void getMedicoById_ok() {
        Medico medico = new Medico();
        medico.setId(1L);

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.of(medico));

        Medico result = medicoService.getMedicoById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }


    @Test
    void getMedicoById_noExiste_lanzaExcepcion() {
        when(medicoRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                MedicoNotFoundException.class,
                () -> medicoService.getMedicoById(1L)
        );
    }

    @Test
    void altaMedico_ok() {
        MedicoDTO dto = new MedicoDTO();
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setDni("123");
        dto.setMatricula("MAT123");
        dto.setEmail("juan@test.com");
        dto.setPassword("1234");
        dto.setEspecialidadDTOList(List.of(1L));

        Especialidad esp = new Especialidad();
        esp.setId(1L);

        when(medicoRepository.findByMatriculaAndDni("MAT123", "123"))
                .thenReturn(Optional.empty());

        when(especialidadRepository.findById(1L))
                .thenReturn(Optional.of(esp));

        when(auth0Service.crearUsuario(anyString(), anyString()))
                .thenReturn("auth0-id");

        when(auth0Properties.getRoleMedico())
                .thenReturn("ROLE_MEDICO");

        when(medicoRepository.save(any(Medico.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Medico medico = medicoService.altaMedico(dto);

        assertNotNull(medico);
        assertEquals("Juan", medico.getNombre());
        assertEquals("auth0-id", medico.getAuth0Id());
    }


    @Test
    void altaMedico_duplicado_lanzaExcepcion() {
        MedicoDTO dto = new MedicoDTO();
        dto.setDni("123");
        dto.setMatricula("MAT123");

        when(medicoRepository.findByMatriculaAndDni("MAT123", "123"))
                .thenReturn(Optional.of(new Medico()));

        assertThrows(
                MedicoDuplicadoException.class,
                () -> medicoService.altaMedico(dto)
        );

        verify(auth0Service, never()).crearUsuario(any(), any());
    }

    @Test
    void altaMedico_especialidadInexistente_lanzaExcepcion() {
        MedicoDTO dto = new MedicoDTO();
        dto.setMatricula("MAT1");
        dto.setDni("123");
        dto.setEspecialidadDTOList(List.of(99L));

        when(medicoRepository.findByMatriculaAndDni(any(), any()))
                .thenReturn(Optional.empty());

        when(especialidadRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EspecialidadNotFoundException.class,
                () -> medicoService.altaMedico(dto)
        );
    }

    @Test
    void bajaMedico_ok() {
        Medico medico = new Medico();
        medico.setId(1L);
        medico.setAuth0Id("auth0-id");

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.of(medico));

        doNothing().when(auth0Service).eliminarUsuario("auth0-id");

        boolean result = medicoService.bajaMedico(1L);

        assertTrue(result);
    }













}
