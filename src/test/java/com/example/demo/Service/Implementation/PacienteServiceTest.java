package com.example.demo.Service.Implementation;

import com.example.demo.DTOs.Request.PacienteDTO;
import com.example.demo.DTOs.Request.UpdatePacienteDTO;
import com.example.demo.Entities.Genero;
import com.example.demo.Entities.Paciente;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Exception.ResourceAlreadyExistsException;
import com.example.demo.Repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private PacienteDTO pacienteDTO;
    private Paciente paciente;
    private UpdatePacienteDTO updatePacienteDTO;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
        pacienteDTO = new PacienteDTO();
        pacienteDTO.setNombre("Juan");
        pacienteDTO.setApellido("Pérez");
        pacienteDTO.setDni("12345678");
        pacienteDTO.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        pacienteDTO.setGenero(Genero.MASCULINO);
        pacienteDTO.setNumero("123456789");
        pacienteDTO.setEmail("juan@example.com");
        pacienteDTO.setDireccion("Calle Falsa 123");

        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNombre("Juan");
        paciente.setApellido("Pérez");
        paciente.setDni("12345678");
        paciente.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        paciente.setGenero(Genero.MASCULINO);
        paciente.setNumero("123456789");
        paciente.setEmail("juan@example.com");
        paciente.setDireccion("Calle Falsa 123");
        paciente.setEstado(true);

        updatePacienteDTO = new UpdatePacienteDTO();
    }

    @Test
    void altaPaciente_CuandoPacienteNoExiste_DeberiaCrearPaciente() {
        // Arrange
        when(pacienteRepository.findByDni(anyString())).thenReturn(Optional.empty());
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // Act
        Paciente resultado = pacienteService.altaPaciente(pacienteDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(pacienteDTO.getNombre(), resultado.getNombre());
        assertEquals(pacienteDTO.getApellido(), resultado.getApellido());
        assertEquals(pacienteDTO.getDni(), resultado.getDni());
        verify(pacienteRepository, times(1)).findByDni(pacienteDTO.getDni());
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    void altaPaciente_CuandoPacienteYaExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(pacienteRepository.findByDni(anyString())).thenReturn(Optional.of(paciente));

        // Act & Assert
        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> pacienteService.altaPaciente(pacienteDTO)
        );

        assertTrue(exception.getMessage().contains(pacienteDTO.getDni()));
        verify(pacienteRepository, times(1)).findByDni(pacienteDTO.getDni());
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    void modificarPaciente_CuandoPacienteExiste_DeberiaModificarTodosLosCampos() {
        // Arrange
        updatePacienteDTO.setNombre("María");
        updatePacienteDTO.setApellido("González");
        updatePacienteDTO.setGenero(Genero.FEMENINO);
        updatePacienteDTO.setNumero("987654321");
        updatePacienteDTO.setEmail("maria@example.com");
        updatePacienteDTO.setDireccion("Av. Siempre Viva 742");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        // Cambio: usar thenAnswer para devolver el mismo objeto que se está guardando
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Paciente resultado = pacienteService.modificarPaciente(updatePacienteDTO, 1L);

        // Assert
        assertNotNull(resultado);
        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteRepository, times(1)).save(paciente);
        assertEquals("María", resultado.getNombre());
        assertEquals("González", resultado.getApellido());
        assertEquals(Genero.FEMENINO, resultado.getGenero());
        assertEquals("987654321", resultado.getNumero());
        assertEquals("maria@example.com", resultado.getEmail());
        assertEquals("Av. Siempre Viva 742", resultado.getDireccion());
    }

    @Test
    void modificarPaciente_CuandoSoloModificaAlgunosCampos_DeberiaMantenerLosOtros() {
        // Arrange
        String apellidoOriginal = paciente.getApellido();
        Genero generoOriginal = paciente.getGenero();

        updatePacienteDTO.setNombre("Carlos");
        updatePacienteDTO.setEmail("carlos@example.com");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Paciente resultado = pacienteService.modificarPaciente(updatePacienteDTO, 1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
        assertEquals("carlos@example.com", resultado.getEmail());
        assertEquals(apellidoOriginal, resultado.getApellido()); // No debería cambiar
        assertEquals(generoOriginal, resultado.getGenero()); // No debería cambiar
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    void modificarPaciente_CuandoPacienteNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> pacienteService.modificarPaciente(updatePacienteDTO, 1L)
        );

        assertTrue(exception.getMessage().contains("1"));
        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    void bajaPaciente_CuandoPacienteExiste_DeberiaDesactivarPaciente() {
        // Arrange
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // Act
        pacienteService.bajaPaciente(1L);

        // Assert
        assertFalse(paciente.getEstado());
        assertNotNull(paciente.getFechaBaja());
        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    void bajaPaciente_CuandoPacienteNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> pacienteService.bajaPaciente(1L)
        );

        assertTrue(exception.getMessage().contains("1"));
        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    void habilitarPaciente_CuandoPacienteExiste_DeberiaActivarPaciente() {
        // Arrange
        paciente.setEstado(false);
        paciente.setFechaBaja(LocalDateTime.now());

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // Act
        Paciente resultado = pacienteService.habilitarPaciente(1L);

        // Assert
        assertNotNull(resultado);
        assertTrue(paciente.getEstado());
        assertNull(paciente.getFechaBaja());
        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteRepository, times(1)).save(paciente);
    }

    @Test
    void habilitarPaciente_CuandoPacienteNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> pacienteService.habilitarPaciente(1L)
        );

        assertTrue(exception.getMessage().contains("1"));
        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }
}