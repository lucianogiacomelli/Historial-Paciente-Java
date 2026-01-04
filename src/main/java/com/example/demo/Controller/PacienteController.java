package com.example.demo.Controller;


import com.example.demo.DTOs.Request.PacienteDTO;
import com.example.demo.DTOs.Request.UpdatePacienteDTO;
import com.example.demo.DTOs.Response.PacienteResponseDTO;
import com.example.demo.Entities.Paciente;
import com.example.demo.Mapper.PacienteMapper;
import com.example.demo.Service.Interface.IPacienteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {
    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);

    @Autowired
    private IPacienteService pacienteService;


    @PreAuthorize("hasAnyAuthority('administrador', 'medico')")
    @PostMapping("/alta-paciente")
    public ResponseEntity<?> altaPaciente(@Valid @RequestBody PacienteDTO pacienteDTO){
            Paciente paciente = pacienteService.altaPaciente(pacienteDTO);
            PacienteResponseDTO response = PacienteMapper.toDTO(paciente);
            logger.info("Se dio de alta al paciente con dni: {}", paciente.getDni());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyAuthority('administrador', 'medico')")
    @PutMapping("/modificar-paciente/{id}")
    public ResponseEntity<?> modificarPaciente(@Valid @RequestBody UpdatePacienteDTO updatePacienteDTO, @RequestParam Long id){
            Paciente paciente = pacienteService.modificarPaciente(updatePacienteDTO, id);
            PacienteResponseDTO response = PacienteMapper.toDTO(paciente);
            logger.info("Se modificó al paciente con id: {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PreAuthorize("hasAnyAuthority('administrador', 'medico')")
    @DeleteMapping("/baja-paciente/{id}")
    public ResponseEntity<?> bajaPaciente(@PathVariable Long id){
            pacienteService.bajaPaciente(id);
            logger.info("Se dio de baja al paciente con id: {}", id);
            return ResponseEntity.status(HttpStatus.OK).body("Se dio de baja al paciente con id: " + id);

    }

    @PreAuthorize("hasAnyAuthority('administrador', 'medico')")
    @PutMapping("/habilitar-paciente/{id}")
    public ResponseEntity<?> habilitarPaciente(@PathVariable Long id){
            Paciente paciente = pacienteService.habilitarPaciente(id);
            logger.info("Se habilitó nuevamente al paciente con id: {}", id);
            PacienteResponseDTO response = PacienteMapper.toDTO(paciente);
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
